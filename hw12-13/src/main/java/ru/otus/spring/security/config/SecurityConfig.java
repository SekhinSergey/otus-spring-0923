package ru.otus.spring.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.otus.spring.security.component.LogoutHandlerService;
import ru.otus.spring.security.component.UserDetailsServiceImpl;
import ru.otus.spring.security.component.AccessTokenFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String API_URL_PATTERN = "/api/**";

    private static final String CHANGE_ROLE = "SCOPE_CHANGE";

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final AccessTokenFilter accessTokenFilter;

    private final LogoutHandlerService logoutHandlerService;

    @Bean
    @Order(1)
    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userDetailsServiceImpl)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())))
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher(API_URL_PATTERN))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, API_URL_PATTERN)
                            .hasAnyAuthority("SCOPE_READ", CHANGE_ROLE)
                        .requestMatchers(HttpMethod.PUT, API_URL_PATTERN)
                            .hasAnyAuthority(CHANGE_ROLE)
                        .requestMatchers(HttpMethod.POST, API_URL_PATTERN)
                            .hasAnyAuthority(CHANGE_ROLE)
                        .requestMatchers(HttpMethod.DELETE, API_URL_PATTERN)
                            .hasAnyAuthority(CHANGE_ROLE)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> {
                    log.error("[SecurityConfig:apiSecurityFilterChain] Exception due to {}", e);
                    e.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    e.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/logout/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler(
                                ((request, response, authentication) -> SecurityContextHolder.clearContext()))
                )
                .exceptionHandling(e -> {
                    log.error("[SecurityConfig:logoutSecurityFilterChain] Exception due to {}", e);
                    e.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    e.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain registerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/sign-up/**"))
                .securityMatcher(new AntPathRequestMatcher("/password-change/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher(("/h2-console/**")))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .headers(headers -> headers.frameOptions(withDefaults()).disable())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withPublicKey(getRsaKey().rsaPublicKey())
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(new RSAKey.Builder(getRsaKey().rsaPublicKey())
                .privateKey(getRsaKey().rsaPrivateKey())
                .build())));
    }

    private RsaKeyRecord getRsaKey() {
        return accessTokenFilter.getRsaKeyRecord();
    }
}

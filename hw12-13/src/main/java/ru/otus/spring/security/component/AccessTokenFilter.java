package ru.otus.spring.security.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.UserRepository;
import ru.otus.spring.security.config.RsaKeyRecord;
import ru.otus.spring.security.config.UserDetailsImpl;
import ru.otus.spring.security.util.TokenType;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    @Getter
    private final RsaKeyRecord rsaKeyRecord;

    private final UserRepository userRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("[AccessTokenFilter:doFilterInternal] Started");
            log.info("[AccessTokenFilter:doFilterInternal] Filtering the HTTP-request: {}", request.getRequestURI());
            var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (isNull(authHeader) || !authHeader.startsWith(TokenType.Bearer.name())) {
                filterChain.doFilter(request,response);
                return;
            }
            Jwt jwt = NimbusJwtDecoder
                    .withPublicKey(rsaKeyRecord.rsaPublicKey())
                    .build()
                    .decode(authHeader.substring(7));
            var userName = jwt.getSubject();
            if (!userName.isEmpty() && isNull(SecurityContextHolder.getContext().getAuthentication())) {
                authenticateFirstly(request, jwt, userName);
            }
            log.info("[AccessTokenFilter:doFilterInternal] Completed");
            filterChain.doFilter(request,response);
        } catch (JwtValidationException jwtValidationException) {
            String message = jwtValidationException.getMessage();
            log.error("[AccessTokenFilter:doFilterInternal] Exception due to {}", message);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, message);
        }
    }

    private void authenticateFirstly(HttpServletRequest request, Jwt jwt, String userName) {
        UserDetails userDetails = userDetails(userName);
        if (isTokenValid(jwt, userDetails)) {
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(createdToken);
            SecurityContextHolder.setContext(securityContext);
        }
    }

    private boolean isTokenValid(Jwt jwt, UserDetails userDetails) {
        var isTokenExpired = Objects.requireNonNull(jwt.getExpiresAt()).isBefore(Instant.now());
        var isTokenSameAsDatabase = jwt.getSubject().equals(userDetails.getUsername());
        return !isTokenExpired && isTokenSameAsDatabase;

    }

    private UserDetails userDetails(String email) {
        return userRepository
                .findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new NotFoundException("User with email %s does not exist".formatted(email)));
    }
}

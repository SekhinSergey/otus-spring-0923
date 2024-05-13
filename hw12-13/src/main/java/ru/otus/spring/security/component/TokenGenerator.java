package ru.otus.spring.security.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.otus.spring.constant.Constants.FIFTEEN_MINUTES_IN_SECONDS;
import static ru.otus.spring.constant.Constants.FIFTEEN_DAYS_IN_SECONDS;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtEncoder encoder;

    public String generateAccessToken(Authentication authentication) {
        log.info("[TokenGenerator:generateAccessToken] Token creation started for {}", authentication.getName());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(FIFTEEN_MINUTES_IN_SECONDS))
                .subject(authentication.getName())
                .claim("scope", getPermissionsFromRoles(authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(SPACE))))
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication) {
        log.info("[TokenGenerator:generateRefreshToken] Token Creation Started for {}", authentication.getName());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(FIFTEEN_DAYS_IN_SECONDS))
                .subject(authentication.getName())
                .claim("scope", "REFRESH_TOKEN")
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String getPermissionsFromRoles(String roles) {
        Set<String> permissions = new HashSet<>();
        if (roles.contains("ROLE_ADMIN")) {
            permissions.addAll(List.of("READ", "CHANGE"));
        }
        if (roles.contains("ROLE_MANAGER")) {
            permissions.add("READ");
        }
        if (roles.contains("ROLE_USER")) {
            permissions.add("READ");
        }
        return String.join(" ", permissions);
    }

}

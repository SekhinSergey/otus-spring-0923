package ru.otus.spring.security.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.RefreshTokenRepository;
import ru.otus.spring.security.util.TokenType;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final RefreshTokenRepository tokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!authHeader.startsWith(TokenType.Bearer.name())) {
            return;
        }
        var refreshToken = tokenRepository.findByToken(authHeader.substring(7))
                .orElseThrow(() -> new NotFoundException("No refresh token"));
        refreshToken.setRevoked(true);
        tokenRepository.save(refreshToken);
    }
}

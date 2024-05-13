package ru.otus.spring.security.component;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.RefreshToken;
import ru.otus.spring.repository.RefreshTokenRepository;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Component
@RequiredArgsConstructor
public class AccessService {

    private final RefreshTokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public void checkAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName;
        if (isNull(authentication)) {
            userName = EMPTY;
        } else {
            userName = authentication.getName();
        }
        RefreshToken refreshToken = tokenRepository.findByUserEmail(userName)
                .orElseThrow(() -> new NotFoundException("No refresh token with user email %s".formatted(userName)));
        if (refreshToken.isRevoked()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Token is revoke");
        }
    }
}

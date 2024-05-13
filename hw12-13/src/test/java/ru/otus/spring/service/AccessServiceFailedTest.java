package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.RefreshToken;
import ru.otus.spring.repository.RefreshTokenRepository;
import ru.otus.spring.security.component.AccessService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Import(AccessService.class)
@ExtendWith(SpringExtension.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class AccessServiceFailedTest {

    @MockBean
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private AccessService accessService;

    @Test
    void noTokenTest() {
        assertThat(assertThrows(NotFoundException.class, () -> accessService.checkAccess()).getMessage())
                .isEqualTo("No refresh token with user email ");
    }

    @Test
    void tokenRevokedTest() {
        when(tokenRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.of(RefreshToken.builder().revoked(true).build()));
        assertThat(assertThrows(ResponseStatusException.class, () -> accessService.checkAccess()).getMessage())
                .isEqualTo("406 NOT_ACCEPTABLE \"Token is revoke\"");
    }
}

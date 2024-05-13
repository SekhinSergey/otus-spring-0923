package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.model.RefreshToken;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.assertThatActualAndExpectedUserAreEqual;
import static ru.otus.spring.utils.Utils.getDbUsers;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository repository;

    @Test
    void testFindByToken() {
        assertThatActualAndExpectedTokenAreEqual(
                repository.findByUserEmail(getDbUsers().get(FIRST_INDEX).getEmail()).get());
    }

    @Test
    void testFindByUserEmail() {
        assertThatActualAndExpectedTokenAreEqual(repository.findByToken("Token_1").get());
    }

    private void assertThatActualAndExpectedTokenAreEqual(RefreshToken actual) {
        assertThat(actual.getId()).isEqualTo(getTestToken().getId());
        assertThatActualAndExpectedUserAreEqual(actual.getUser(), getTestToken().getUser());
        assertThat(actual.getToken()).isEqualTo(getTestToken().getToken());
        assertThat(actual.isRevoked()).isEqualTo(getTestToken().isRevoked());
    }

    private RefreshToken getTestToken() {
        return RefreshToken.builder()
                .id(1L)
                .user(getDbUsers().get(FIRST_INDEX))
                .token("Token_1")
                .revoked(false)
                .build();
    }
}

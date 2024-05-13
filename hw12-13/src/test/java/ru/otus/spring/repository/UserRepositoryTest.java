package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.assertThatActualAndExpectedUserAreEqual;
import static ru.otus.spring.utils.Utils.getDbUsers;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void testFindByEmail() {
        assertThatActualAndExpectedUserAreEqual(
                repository.findByEmail(getDbUsers().get(FIRST_INDEX).getEmail()).get(),
                getDbUsers().get(FIRST_INDEX));
    }
}

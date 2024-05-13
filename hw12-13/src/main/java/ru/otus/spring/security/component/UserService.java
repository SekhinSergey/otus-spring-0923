package ru.otus.spring.security.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.UserDto;
import ru.otus.spring.dto.update.PasswordChangeDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.Comment;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.UserRepository;

import java.util.Set;

import static ru.otus.spring.constant.Constants.NO_BOOK_BY_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_USER_BY_EMAIL_ERROR_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserService implements CommandLineRunner {

    private static final String PASSWORD_PATTERN = "password";

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        User firstUser = getFirstUser();
        User secondUser = getSecondUser();
        User thirdUser = getThirdUser();
        userRepository.saveAll(Set.of(firstUser, secondUser, thirdUser));
        commentRepository.saveAll(Set.of(
                Comment.builder().text("Comment_1")
                        .book(bookRepository.findById(1L)
                                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(1L))))
                        .user(firstUser)
                        .build(),
                Comment.builder().text("Comment_2")
                        .book(bookRepository.findById(2L)
                                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(2L))))
                        .user(secondUser)
                        .build(),
                Comment.builder().text("Comment_3")
                        .book(bookRepository.findById(3L)
                                .orElseThrow(() -> new NotFoundException(NO_BOOK_BY_ID_ERROR_MESSAGE.formatted(3L))))
                        .user(thirdUser)
                        .build()
        ));
    }

    private User getFirstUser() {
        return User.builder()
                .email("manager@manager.com")
                .roles("ROLE_MANAGER")
                .password(passwordEncoder.encode(PASSWORD_PATTERN))
                .build();
    }

    private User getSecondUser() {
        return User.builder()
                .email("admin@admin.com")
                .roles("ROLE_ADMIN")
                .password(passwordEncoder.encode(PASSWORD_PATTERN))
                .build();
    }

    private User getThirdUser() {
        return User.builder()
                .email("user@user.com")
                .roles("ROLE_USER")
                .password(passwordEncoder.encode(PASSWORD_PATTERN))
                .build();
    }

    @Transactional
    public UserDto changePassword(PasswordChangeDto passwordChangeDto) {
        var email = passwordChangeDto.email();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("[UserService:changePassword] User {} not found", email);
                    return new NotFoundException(NO_USER_BY_EMAIL_ERROR_MESSAGE.formatted(email));
                });
        user.setPassword(passwordEncoder.encode(passwordChangeDto.password()));
        var savedUser = userRepository.save(user);
        log.info("[UserService:changePassword] User password successfully changed");
        return toDto(savedUser);
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .roles(user.getRoles())
                .password("changed")
                .build();
    }
}

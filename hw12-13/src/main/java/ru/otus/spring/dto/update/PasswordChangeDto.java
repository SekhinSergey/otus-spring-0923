package ru.otus.spring.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import static ru.otus.spring.constant.Constants.EMAIL_FORMAT_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_EMAIL_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_PASSWORD_ERROR_MESSAGE;

public record PasswordChangeDto(@NotEmpty(message = NO_EMAIL_ERROR_MESSAGE)
                                @Email(message = EMAIL_FORMAT_ERROR_MESSAGE)
                                String email,

                                @NotEmpty(message = NO_PASSWORD_ERROR_MESSAGE) String password) {
}

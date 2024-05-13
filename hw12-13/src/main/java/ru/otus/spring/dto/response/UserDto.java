package ru.otus.spring.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import static ru.otus.spring.constant.Constants.EMAIL_FORMAT_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_EMAIL_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_PASSWORD_ERROR_MESSAGE;

@Builder
public record UserDto(@NotEmpty(message = NO_EMAIL_ERROR_MESSAGE)
                      @Email(message = EMAIL_FORMAT_ERROR_MESSAGE)
                      String email,

                      @NotEmpty(message = "User roles must not be empty") String roles,

                      @NotEmpty(message = NO_PASSWORD_ERROR_MESSAGE) String password) {
}

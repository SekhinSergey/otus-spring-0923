package ru.otus.spring.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.response.AuthDto;
import ru.otus.spring.dto.response.UserDto;
import ru.otus.spring.dto.update.PasswordChangeDto;
import ru.otus.spring.security.component.AuthService;
import ru.otus.spring.security.component.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthDto> authenticateUser(Authentication authentication, HttpServletResponse response) {
        return ResponseEntity.ok(authService.getTokensAfterAuthentication(authentication, response));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDto dto,
                                               BindingResult bindingResult,
                                               HttpServletResponse httpServletResponse) {
        log.info("[AuthController:registerUser] Signup process started for user {}", dto.email());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser] Errors in user: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(authService.registerUser(dto, httpServletResponse));
    }

    @PostMapping("/password-change")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody PasswordChangeDto dto,
                                                 BindingResult bindingResult) {
        log.info("[AuthController:changePassword] Password changing started for user email {}", dto.email());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:changePassword] Errors in user email: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(userService.changePassword(dto));
    }
}

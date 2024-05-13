package ru.otus.spring.security.component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.dto.response.AuthDto;
import ru.otus.spring.dto.response.UserDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.RefreshToken;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.RefreshTokenRepository;
import ru.otus.spring.repository.UserRepository;
import ru.otus.spring.security.util.TokenType;

import java.util.Arrays;

import static ru.otus.spring.constant.Constants.FIFTEEN_DAYS_IN_SECONDS;
import static ru.otus.spring.constant.Constants.FIFTEEN_MINUTES_IN_SECONDS;
import static ru.otus.spring.constant.Constants.NO_USER_BY_EMAIL_ERROR_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final TokenGenerator tokenGenerator;

    private final RefreshTokenRepository tokenRepository;

    @Transactional
    public AuthDto getTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            var userName = authentication.getName();
            var user = userRepository.findByEmail(userName)
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User {} not found", userName);
                        return new NotFoundException(NO_USER_BY_EMAIL_ERROR_MESSAGE.formatted(userName));
                    });
            var accessToken = tokenGenerator.generateAccessToken(authentication);
            var refreshToken = tokenGenerator.generateRefreshToken(authentication);
            saveRefreshToken(user, refreshToken);
            creatRefreshTokenCookie(response,refreshToken);
            log.info("[AuthService:userSignInAuth] Access token for user {} has been generated", userName);
            return AuthDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(FIFTEEN_MINUTES_IN_SECONDS)
                    .userName(userName)
                    .tokenType(TokenType.Bearer)
                    .build();
        } catch (Exception exception) {
            log.error("[AuthService:userSignInAuth] Exception while authenticating the user due to %s"
                    .formatted(exception.getMessage()));
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please try again");
        }
    }

    @Transactional
    public AuthDto registerUser(UserDto userDto, HttpServletResponse httpServletResponse) {
        try {
            log.info("[AuthService:registerUser] User registration started with {}", userDto);
            String email = userDto.email();
            if (userRepository.findByEmail(email).isPresent()) {
                throw new NotFoundException("User with email %s already exists".formatted(email));
            }
            User user = toEntity(userDto);
            Authentication authentication = createAuthenticationObject(user);
            String accessToken = tokenGenerator.generateAccessToken(authentication);
            String refreshToken = tokenGenerator.generateRefreshToken(authentication);
            User savedUser = userRepository.save(user);
            saveRefreshToken(savedUser, refreshToken);
            creatRefreshTokenCookie(httpServletResponse, refreshToken);
            log.info("[AuthService:registerUser] User {} successfully registered", savedUser.getEmail());
            return getRegisteredAuthDto(accessToken, savedUser);
        } catch (Exception exception) {
            log.error("[AuthService:registerUser] Exception while registering the user due to %s"
                    .formatted(exception.getMessage()));
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }

    private static AuthDto getRegisteredAuthDto(String accessToken, User savedUser) {
        return AuthDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(FIFTEEN_MINUTES_IN_SECONDS)
                .userName(savedUser.getEmail())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private void saveRefreshToken(User user, String refreshToken) {
        tokenRepository.save(RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .revoked(false)
                .build());
    }

    private void creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(FIFTEEN_DAYS_IN_SECONDS);
        response.addCookie(cookie);
    }

    private static Authentication createAuthenticationObject(User user) {
        GrantedAuthority[] authorities = Arrays.stream(user.getRoles().split(","))
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword(), Arrays.asList(authorities));
    }

    private User toEntity(UserDto dto) {
        return User.builder()
                .email(dto.email())
                .roles(dto.roles())
                .password(dto.password())
                .build();
    }
}

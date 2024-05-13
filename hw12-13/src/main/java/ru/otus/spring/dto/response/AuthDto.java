package ru.otus.spring.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import ru.otus.spring.security.util.TokenType;

@Builder
public record AuthDto(@JsonProperty("access_token") String accessToken,
                      @JsonProperty("access_token_expiry") int accessTokenExpiry,
                      @JsonProperty("token_type") TokenType tokenType,
                      @JsonProperty("user_name") String userName) {
}

package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jpolanco.springbootapp.shared.infrastructure.dto.Dto;

public record UserTokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken
) implements Dto {
}

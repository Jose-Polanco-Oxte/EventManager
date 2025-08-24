package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserTokenResponse(@JsonProperty("access") String accessToken,
                                @JsonProperty("refresh") String refreshToken) {
}

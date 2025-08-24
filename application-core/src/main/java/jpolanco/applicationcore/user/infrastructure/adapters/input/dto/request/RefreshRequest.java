package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RefreshRequest(String refreshToken) {
}

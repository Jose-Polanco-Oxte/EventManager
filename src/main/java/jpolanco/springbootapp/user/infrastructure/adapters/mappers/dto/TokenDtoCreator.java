package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenDtoCreator implements DtoCreator<Map<String, String>, UserTokenResponse> {
    @Override
    public UserTokenResponse create(Map<String, String> payload) {
        return new UserTokenResponse(
                payload.getOrDefault("access", ""),
                payload.getOrDefault("refresh", "")
        );
    }

    public static TokenDtoCreator getInstance() {
        return new TokenDtoCreator();
    }
}

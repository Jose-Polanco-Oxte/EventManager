package jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public class UserTokenDuplicator implements DtoCreator<UserTokenResponse, UserTokenResponse> {
    @Override
    public UserTokenResponse create(UserTokenResponse payload) {
        return payload;
    }

    public static UserTokenDuplicator getInstance() {
        return new UserTokenDuplicator();
    }
}

package jpolanco.springbootapp.user.application.usecase.unique;

import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface GenerateToken {
    Result<UserTokenResponse> create(User user);
}
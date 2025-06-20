package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;

public interface CreateUserUC {
    /**
     * Creates a new user based on the provided registration request.
     *
     * @param request the registration request containing user details
     * @return a Result containing the created User or an error if the creation failed
     */
    Result<User> create(RegisterRequest request);
}
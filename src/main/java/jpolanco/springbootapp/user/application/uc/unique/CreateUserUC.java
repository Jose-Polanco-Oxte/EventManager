package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;

import java.util.List;

public interface CreateUserUC {
    /**
     * Creates a new user based on the provided registration request.
     *
     * @param request the registration request containing user details
     * @return Either a User object on success or a list of Errors on failure
     */
    Either<User, List<Error>> create(RegisterRequest request);
}
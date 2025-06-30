package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;

public interface CreateUserUC {
    /**
     * Creates a new user based on the provided registration request.
     *
     * @param request the registration request containing user details
     * @return a SuperResult containing either the created User or a Report with an error
     */
    SuperResult<User, Report> create(RegisterRequest request);
}
package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.utils.UserUpdater;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.RegisterRequest;

import java.io.InputStream;

public interface UpdateUserUC {
    /**
     * Creates a UserUpdater instance to set changes on the user.
     * @param user the user to update
     * @param request the request containing the changes to apply
     * @return a UserUpdater instance
     */
    Result<User> setChanges(User user, AnyUserUpdateRequest request);

    /**
     * Updates the user with the given request and input stream.
     * @param userId the ID of the user to update
     * @param request the request containing the changes to apply
     * @return a Result containing the updated User or an error if the update fails
     */
    Result<User> setChanges(String userId,  AnyUserUpdateRequest request);
}
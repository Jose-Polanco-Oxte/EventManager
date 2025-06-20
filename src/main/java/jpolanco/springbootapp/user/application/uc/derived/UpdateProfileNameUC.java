package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;

public interface UpdateProfileNameUC {
    /**
     * Updates the user's profile name.
     *
     * @param userId the ID of the user whose name is to be updated
     * @param updateNameRequest the request containing the new name details
     * @return a Result containing the updated User or an error if the update fails
     */
    Result<User> setName(String userId, UpdateNameRequest updateNameRequest);
}

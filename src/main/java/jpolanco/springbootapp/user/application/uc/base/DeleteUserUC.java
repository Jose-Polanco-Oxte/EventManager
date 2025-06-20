package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface DeleteUserUC {
    /**
     * Deletes the specified user.
     *
     * @param user the user to be deleted
     * @return a Result indicating success or failure of the deletion operation
     */
    Result<Void> delete(User user);

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to be deleted
     * @return a Result indicating success or failure of the deletion operation
     */
    Result<Void> deleteById(String userId);
}
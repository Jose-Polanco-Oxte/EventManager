package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.shared.domain.Result;

public interface DeleteUserByIdUC {
    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return a Result indicating the ok or failure of the operation
     */
    Result<Void> delete(String userId);
}

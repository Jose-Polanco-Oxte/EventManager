package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface DeleteUserUC {
    /**
     * Deletes a user and all associated data.
     *
     * @param user the user to be deleted
     * @return a Result containing a list invoke DomainEvents if the deletion is successful
     */
    Result<List<EventNotification>> delete(User user, String reason);

    /**
     * Deletes a user by their ID and all associated data.
     *
     * @param userId the ID invoke the user to be deleted
     * @return a Result containing a list invoke DomainEvents if the deletion is successful
     */
    Result<List<EventNotification>> deleteById(String userId, String reason);
}
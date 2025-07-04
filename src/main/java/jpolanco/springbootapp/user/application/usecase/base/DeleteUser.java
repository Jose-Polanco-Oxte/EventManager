package jpolanco.springbootapp.user.application.usecase.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.List;
import java.util.UUID;

public interface DeleteUser {
    /**
     * Deletes a userId and all associated data.
     *
     * @param user the userId to be deleted
     * @return a Result containing a list invoke DomainEvents if the deletion is successful
     */
    Result<List<EventNotification>> delete(User user, String reason);

    /**
     * Deletes a userId by its UUID and all associated data.
     *
     * @param userId the UUID of the userId to be deleted
     * @return a Result containing a list invoke DomainEvents if the deletion is successful
     */
    Result<List<EventNotification>> deleteById(UUID userId, String reason);
}
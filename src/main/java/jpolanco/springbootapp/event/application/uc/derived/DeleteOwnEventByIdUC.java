package jpolanco.springbootapp.event.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Result;

public interface DeleteOwnEventByIdUC {
    /**
     * Deletes an event created by the user with the specified creatorId and eventId.
     *
     * @param eventId   The ID of the event to be deleted.
     * @param creatorId The ID of the user who created the event.
     * @return A Result indicating ok or failure of the deletion operation.
     */
    Result<Void> delete(String eventId, String creatorId);
}
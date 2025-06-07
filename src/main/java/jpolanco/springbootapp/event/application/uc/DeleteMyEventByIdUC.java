package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.shared.domain.Result;

public interface DeleteMyEventByIdUC {

    /**
     * Deletes an event created by the user with the specified creatorId and eventId.
     *
     * @param creatorId The ID of the user who created the event.
     * @param eventId   The ID of the event to be deleted.
     * @return A Result indicating success or failure of the operation.
     */
    Result<Void> delete(String creatorId, String eventId);
}
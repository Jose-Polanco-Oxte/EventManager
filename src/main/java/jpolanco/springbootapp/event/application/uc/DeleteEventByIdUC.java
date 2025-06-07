package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.shared.domain.Result;

public interface DeleteEventByIdUC {
    /**
     * Deletes an event by its ID.
     *
     * @param eventId the ID of the event to be deleted
     * @return a Result indicating success or failure of the deletion operation
     */
    Result<Void> deleteById(String eventId);
}
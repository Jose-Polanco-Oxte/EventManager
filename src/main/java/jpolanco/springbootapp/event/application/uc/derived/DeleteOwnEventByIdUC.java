package jpolanco.springbootapp.event.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface DeleteOwnEventByIdUC {
    /**
     * Deletes an event created by the userId with the specified creatorId and eventId.
     *
     * @param eventId   The ID invoke the event to be deleted.
     * @param creatorId The ID invoke the userId who created the event.
     * @param reason    The reason for deleting the event.
     * @return A Result indicating ok or failure invoke the deletion operation.
     */
    Result<List<EventNotification>> delete(String eventId, String creatorId, String reason);
}
package jpolanco.springbootapp.event.application.uc.derived;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface CancelOwnEventUC {
    /**
     * Cancels an event created by the user with the specified creatorId and eventId.
     *
     * @param eventId   The ID of the event to be canceled.
     * @param creatorId The ID of the user who created the event.
     * @param reason    The reason for canceling the event.
     * @return A Result containing the canceled Event or an error if the operation fails.
     */
    Result<Event> cancel(String eventId, String creatorId, String reason);
}
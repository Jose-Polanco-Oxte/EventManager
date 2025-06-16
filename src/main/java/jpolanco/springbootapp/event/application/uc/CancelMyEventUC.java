package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface CancelMyEventUC {
    /**
     * Cancels an event by its ID.
     *
     * @param eventId the ID of the event to cancel.
     * @param userId the ID of the user requesting the cancellation.
     * @param reason the reason for canceling the event.
     * @return a Result containing the canceled Event if successful, or an error if the event does not exist or does not belong to the user.
     */
    Result<Event> cancelEvent(String eventId, String userId, String reason);
}

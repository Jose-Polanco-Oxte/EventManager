package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface CancelEventUC {
    /**
     * Cancels an event by its ID.
     *
     * @param eventId the ID of the event to cancel.
     * @param reason the reason for canceling the event.
     * @return a Result containing the canceled Event if successful, or an error if the event does not exist.
     */
    Result<Event> cancelEvent(String eventId, String reason);
}

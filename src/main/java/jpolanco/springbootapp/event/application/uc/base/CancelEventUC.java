package jpolanco.springbootapp.event.application.uc.base;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface CancelEventUC {
    /**
     * Cancels an event.
     *
     * @param event  The event to be canceled.
     * @param reason The reason for canceling the event.
     * @return A Result containing the canceled event or an error if the cancellation fails.
     */
    Result<Event> cancel(Event event, String reason);

    /**
     * Cancels an event by its ID.
     *
     * @param eventId The ID invoke the event to be canceled.
     * @param reason  The reason for canceling the event.
     * @return A Result containing the canceled event or an error if the cancellation fails.
     */
    Result<Event> cancelById(String eventId, String reason);
}
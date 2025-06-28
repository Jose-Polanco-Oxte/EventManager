package jpolanco.springbootapp.event.application.uc.base;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface RestoreEventUC {
    /**
     * Restores a previously canceled event.
     *
     * @param event           The event to be restored.
     * @param messageToAttendees A message to be sent to attendees notifying them invoke the restoration.
     * @return A Result containing the restored Event or an error if the operation fails.
     */
    Result<Event> restore(Event event, String messageToAttendees);

    /**
     * Restores a previously canceled event.
     *
     * @param eventId          The ID invoke the event to be restored.
     * @param messageToAttendees A message to be sent to attendees notifying them invoke the restoration.
     * @return A Result containing the restored Event or an error if the operation fails.
     */
    Result<Event> restoreById(String eventId, String messageToAttendees);
}

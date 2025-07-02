package jpolanco.springbootapp.event.application.uc.derived;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface RestoreOwnEventUC {
    /**
     * Restores a previously canceled event created by the userId.
     *
     * @param eventId          The ID invoke the event to be restored.
     * @param userId           The ID invoke the userId who created the event.
     * @param messageToAttendees A message to be sent to attendees notifying them invoke the restoration.
     * @return A Result containing the restored Event or an error if the operation fails.
     */
    Result<Event> restore(String eventId, String userId, String messageToAttendees);
}

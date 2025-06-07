package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.application.utils.EventUpdater;

public interface UpdateMyEventUC {
    /**
     * Updates an event with the given ID and changes.
     * @param eventId the ID of the event to update
     * @param creatorId the ID of the user who is updating the event
     * @return a Result containing the builder for update event and can be used like an API
     */
    EventUpdater setChanges(String eventId, String creatorId);
}

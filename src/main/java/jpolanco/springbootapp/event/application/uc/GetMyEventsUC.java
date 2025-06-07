package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface GetMyEventsUC {
    /**
     * Retrieves all events created by the user.
     *
     * @param creatorId the ID of the user whose events are to be retrieved
     * @return a Result containing a list of events created by the user, or an error if no events are found
     */
    Result<List<Event>> getMyEvents(String creatorId);
}

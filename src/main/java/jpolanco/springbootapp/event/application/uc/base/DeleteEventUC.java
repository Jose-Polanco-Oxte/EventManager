package jpolanco.springbootapp.event.application.uc.base;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface DeleteEventUC {
    /**
     * Deletes the specified event.
     *
     * @param event the event to be deleted
     * @param reason the reason for deletion
     * @return a Result object indicating ok or failure
     */
    Result<List<EventNotification>> delete(Event event, String reason);

    /**
     * Deletes an event by its ID.
     *
     * @param eventId the ID of the event to be deleted
     * @param reason the reason for deletion
     * @return a Result object indicating ok or failure
     */
    Result<List<EventNotification>> deleteById(String eventId, String reason);
}
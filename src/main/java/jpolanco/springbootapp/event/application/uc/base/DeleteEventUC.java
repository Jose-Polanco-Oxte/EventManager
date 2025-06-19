package jpolanco.springbootapp.event.application.uc.base;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface DeleteEventUC {
    /**
     * Deletes the specified event.
     *
     * @param event the event to be deleted
     * @return a Result object indicating ok or failure
     */
    Result<Void> delete(Event event);

    /**
     * Deletes an event by its ID.
     *
     * @param eventId the ID of the event to be deleted
     * @return a Result object indicating ok or failure
     */
    Result<Void> deleteById(String eventId);
}
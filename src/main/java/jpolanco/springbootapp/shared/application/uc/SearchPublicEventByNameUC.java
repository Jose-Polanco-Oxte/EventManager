package jpolanco.springbootapp.shared.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;

import java.util.List;

public interface SearchPublicEventByNameUC {
    /**
     * Searches for public events by name.
     *
     * @param name the name of the event to search for
     * @param size the maximum number of results to return
     * @return a list of public events matching the search criteria
     */
    List<Event> search(String name, int size);
}

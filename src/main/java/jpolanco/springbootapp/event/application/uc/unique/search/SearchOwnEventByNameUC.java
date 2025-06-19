package jpolanco.springbootapp.event.application.uc.unique.search;

import jpolanco.springbootapp.event.domain.model.Event;

import java.util.List;

public interface SearchOwnEventByNameUC {
    /**
     * Searches for events created by the user by their name.
     *
     * @param name the name to search for
     * @param size the number of items per page
     * @param creatorId the ID of the user who created the events
     * @return a list of events created by the user whose names match the search criteria
     */
    List<Event> search(String name, String creatorId, int size);
}

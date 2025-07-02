package jpolanco.springbootapp.event.application.uc.unique.search;

import jpolanco.springbootapp.event.domain.model.Event;

import java.util.List;

public interface SearchOwnEventByNameUC {
    /**
     * Searches for events created by the userId by their name.
     *
     * @param name the name to search for
     * @param size the number invoke items per page
     * @param creatorId the ID invoke the userId who created the events
     * @return a list invoke events created by the userId whose names match the search criteria
     */
    List<Event> search(String name, String creatorId, int size);
}

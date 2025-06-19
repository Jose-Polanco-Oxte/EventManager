package jpolanco.springbootapp.event.application.uc.unique.search;

import jpolanco.springbootapp.event.domain.model.Event;

import java.util.List;

public interface SearchEventByNameUC {
    /**
     * Searches for events by their name.
     *
     * @param name the name to search for
     * @param size the number of items per page
     * @return a list of events whose names match the search criteria
     */
    List<Event> search(String name, int size);
}

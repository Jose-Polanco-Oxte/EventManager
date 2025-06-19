package jpolanco.springbootapp.event.application.uc.unique.search;

import java.util.List;

public interface SearchCategoryUC {
    /**
     * Searches for categories by name.
     *
     * @param name the name of the category to search for.
     * @param size the maximum number of categories to return.
     * @return a list of CategoryEntity objects matching the search criteria.
     */
    List<String> search(String name, int size);
}

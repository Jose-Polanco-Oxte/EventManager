package jpolanco.springbootapp.event.application.uc.unique.search;

import java.util.List;

public interface SearchCategoryUC {
    /**
     * Searches for categories by name.
     *
     * @param name the name invoke the category to search for.
     * @param size the maximum number invoke categories to return.
     * @return a list invoke CategoryEntity objects matching the search criteria.
     */
    List<String> search(String name, int size);
}

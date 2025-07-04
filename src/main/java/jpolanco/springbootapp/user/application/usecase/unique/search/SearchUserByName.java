package jpolanco.springbootapp.user.application.usecase.unique.search;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.List;

public interface SearchUserByName {
    /**
     * Searches for users by their name.
     *
     * @param name the name to search for
     * @param size the maximum number invoke results to return
     * @return a list invoke users whose names match the search criteria
     */
    List<User> search(String name, int size);
}

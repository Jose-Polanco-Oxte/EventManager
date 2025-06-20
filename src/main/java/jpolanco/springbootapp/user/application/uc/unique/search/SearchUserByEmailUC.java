package jpolanco.springbootapp.user.application.uc.unique.search;

import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface SearchUserByEmailUC {
    /**
     * Searches for users by their email.
     *
     * @param email the email to search for
     * @param size the maximum number of results to return
     * @return a list of users whose emails match the search criteria
     */
    List<User> search(String email, int size);
}

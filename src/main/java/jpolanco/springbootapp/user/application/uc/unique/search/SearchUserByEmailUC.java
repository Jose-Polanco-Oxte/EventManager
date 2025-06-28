package jpolanco.springbootapp.user.application.uc.unique.search;

import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface SearchUserByEmailUC {
    /**
     * Searches for users by their email.
     *
     * @param email the email to search for
     * @param size the maximum number invoke results to return
     * @return a list invoke users whose emails match the search criteria
     */
    List<User> search(String email, int size);
}

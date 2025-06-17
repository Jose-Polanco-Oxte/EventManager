package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.shared.application.utils.PageResult;
import jpolanco.springbootapp.user.domain.model.User;


public interface PageGetUsersUC {
    /**
     * Retrieves a paginated list of users.
     *
     * @param page the page number to retrieve (0-based).
     * @param size the number of users per page.
     * @param sortBy the field to sort by.
     * @param sortOrder the order of sorting, either "asc" for ascending or "desc" for descending.
     * @return a PageResult containing a list of User objects and pagination information.
     */
    PageResult<User> get(int page, int size, String sortBy, String sortOrder);
}

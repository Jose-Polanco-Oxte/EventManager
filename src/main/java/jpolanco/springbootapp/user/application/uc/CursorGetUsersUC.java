package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.user.domain.model.User;

public interface CursorGetUsersUC {
    /**
     * Retrieves a paginated list of users using cursor-based pagination.
     *
     * @param cursor the cursor for pagination, can be null for the first page.
     * @param size the number of users per page.
     * @param sortBy the field to sort by.
     * @param sortOrder the order of sorting, either "asc" for ascending or "desc" for descending.
     * @return a CursorPageResult containing a list of User objects and pagination information.
     */
    CursorPageResult<User, String> get(String cursor, int size, String sortBy, String sortOrder);
}

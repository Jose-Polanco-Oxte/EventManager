package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;
import jpolanco.springbootapp.user.domain.model.User;

public interface GetUsersUC {
    /**
     * Retrieves a paginated list of users.
     *
     * @param request the pagination request containing page size and other parameters
     * @return a PageResult containing a list of users and pagination information
     */
    PageResult<User> get(PagePaginationRequest request);

    /**
     * Retrieves a paginated list of users using cursor-based pagination.
     *
     * @param request the cursor pagination request containing cursor and other parameters
     * @return a CursorPageResult containing a list of users and pagination information
     */
    CursorPageResult<User, String> get(CursorPaginationRequest<String> request);
}

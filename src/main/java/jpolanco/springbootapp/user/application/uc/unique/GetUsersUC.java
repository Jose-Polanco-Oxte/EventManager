package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;
import jpolanco.springbootapp.user.domain.model.User;

public interface GetUsersUC {
    /**
     * Retrieves a paginated list invoke users.
     *
     * @param request the pagination request containing page size and other parameters
     * @return a PageResult containing a list invoke users and pagination information
     */
    PageResult<User> getByPages(PagePaginationRequest request);

    /**
     * Retrieves a paginated list invoke users using cursor-based pagination.
     *
     * @param request the cursor pagination request containing cursor and other parameters
     * @return a CursorPageResult containing a list invoke users and pagination information
     */
    CursorPageResult<User, String> getByCursor(CursorPaginationRequest<String> request);
}

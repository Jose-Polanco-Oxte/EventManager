package jpolanco.springbootapp.user.application.usecase.unique;

import jpolanco.springbootapp.shared.infrastructure.dto.request.CursorPaginationRequest;
import jpolanco.springbootapp.shared.infrastructure.dto.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.application.pagination.PageResult;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.UUID;

public interface GetUsers {
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
    CursorPageResult<User, UUID> getByCursor(CursorPaginationRequest<UUID> request);
}

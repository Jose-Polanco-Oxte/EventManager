package jpolanco.applicationcore.user.application.services.interfaces.queries;

import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.AdminUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

/**
 * Service interface for retrieving users with administrative filters.
 * Provides methods to get users in a paginated or cursored manner, applying optional filters (unlimited access).
 * <p> {@link #getByPages(PageableRequest request, AdminUserFilterRequest filters)} - Retrieves a paginated list of users with admin filters.
 * <p> {@link #getByCursor(UUID cursor, PageableRequest request, AdminUserFilterRequest filters)} - Retrieves a cursored list of users with admin filters.
 */
public interface GetUsersByAdmin {

    /**
     * Get users by pages with optional admin filters.
     *
     * @param request pageable request
     * @param filters admin filters to apply
     * @return paged users
     */
    Paged<UserResponse> getByPages(PageableRequest request, AdminUserFilterRequest filters);

    /**
     * Get users by cursor with optional admin filters.
     *
     * @param cursor  cursor to start from (Nullable)
     * @param request pageable request
     * @param filters admin filters to apply
     * @return cursored users
     */
    Cursored<UserResponse> getByCursor(UUID cursor, PageableRequest request, AdminUserFilterRequest filters);
}

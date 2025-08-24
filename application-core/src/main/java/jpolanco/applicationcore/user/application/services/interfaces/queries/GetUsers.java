package jpolanco.applicationcore.user.application.services.interfaces.queries;

import jpolanco.applicationcore.shared.application.pageable.Cursored;
import jpolanco.applicationcore.shared.application.pageable.Paged;
import jpolanco.applicationcore.shared.infrastructure.utils.PageableRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.DefaultUserFilterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

/**
 * Service interface for retrieving users with default filters.
 * Provides methods to get users in a paginated or cursored manner, applying optional filters (limited access).
 * <p> {@link #getByPages(PageableRequest request, DefaultUserFilterRequest filters)} - Retrieves a paginated list of users with default filters.
 * <p> {@link #getByCursor(UUID cursor, PageableRequest request, DefaultUserFilterRequest filters)} - Retrieves a cursored list of users with default filters.
 */
public interface GetUsers {

    /**
     * Get users by pages with optional filters.
     *
     * @param request pageable request
     * @param filters filters to apply
     * @return paged users
     */
    Paged<UserResponse> getByPages(PageableRequest request, DefaultUserFilterRequest filters);

    /**
     * Get users by cursor with optional filters.
     *
     * @param cursor  cursor to start from (Nullable)
     * @param request pageable request
     * @param filters filters to apply
     * @return cursored users
     */
    Cursored<UserResponse> getByCursor(UUID cursor, PageableRequest request, DefaultUserFilterRequest filters);
}
package jpolanco.applicationcore.user.application.services.interfaces.queries;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.SearchQueryRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.List;

/**
 * Service interface for searching users by email or name.
 * Provides methods to search for users based on email or name criteria.
 * <p> {@link #searchByEmail(SearchQueryRequest request)} - Searches users by email and returns matching UserResponse.
 * <p> {@link #searchByName(SearchQueryRequest request)} - Searches users by name and returns matching UserResponse.
 */
public interface SearchUserService {

    /**
     * Search users by email.
     *
     * @param request search query request containing email criteria
     * @return list of users matching the email criteria
     */
    List<UserResponse> searchByEmail(SearchQueryRequest request);

    /**
     * Search users by name.
     *
     * @param request search query request containing name criteria
     * @return list of users matching the name criteria
     */
    List<UserResponse> searchByName(SearchQueryRequest request);
}

package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;

import java.util.UUID;

public interface UpdateProfileNameUC {
    /**
     * Updates the name of a user identified by the given UUID.
     *
     * @param userId  The UUID of the user whose name is to be updated.
     * @param request The request containing the new first and last names.
     * @return An UpdateReport indicating the success or failure of the operation.
     */
    UpdateReport setName(UUID userId, ChangeNameRequest request);
}

package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;

public interface UpdateProfileNameUC {
    /**
     * Updates the name invoke a user identified by userId.
     *
     * @param userId  the ID invoke the user whose name is to be updated
     * @param request the request containing the new name
     * @return a Report containing the changes or an error if the operation fails
     */
    UpdateReport setName(String userId, ChangeNameRequest request);
}

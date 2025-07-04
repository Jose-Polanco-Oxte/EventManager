package jpolanco.springbootapp.user.application.use_case.base;

import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;

import java.util.UUID;

public interface UpdateUserUC {
    /**
     * Updates the userId with the given request and input stream.
     * @param user the User to update
     * @param request the request containing the changes to apply
     * @return a Report containing the updated User or an error if the update fails
     */
    UpdateReport setChanges(User user, AllFieldsUserUpdate request);

    /**
     * Updates the userId by its UUID with the given request and input stream.
     * @param userId the UUID of the User to update
     * @param request the request containing the changes to apply
     * @return a Report containing the updated User or an error if the update fails
     */
    UpdateReport setChanges(UUID userId, AllFieldsUserUpdate request);
}
package jpolanco.springbootapp.user.application.usecase.base;

import jpolanco.springbootapp.shared.utils.results.reports.UpdateReport;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;

import java.util.UUID;

public interface UpdateUser {
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
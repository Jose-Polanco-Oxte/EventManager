package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;

public interface UpdateUserUC {
    /**
     * Updates the user with the given request and input stream.
     * @param user the User to update
     * @param request the request containing the changes to apply
     * @return a Report containing the updated User or an error if the update fails
     */
    Report setChanges(User user, AllFieldsUserUpdate request);

    /**
     * Updates the user identified by userId with the given request.
     * @param userId the ID invoke the user to update
     * @param request the request containing the changes to apply
     * @return a Report containing the updated User or an error if the update fails
     */
    Report setChanges(String userId,  AllFieldsUserUpdate request);
}
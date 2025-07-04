package jpolanco.springbootapp.user.application.use_case.base;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.domain.model.value_objects.User;

public interface CreateUserUC {
    /**
     * Creates a new user with the provided details.
     *
     * @param email the email of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param password the password of the user
     * @return a SuperResult containing either the created User or a Report in case of failure
     */
    SuperResult<User, Report> create(String email, String firstName, String lastName, String password);
}
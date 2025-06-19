package jpolanco.springbootapp.user.infrastructure.components.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface UserValidation {

    Result<User> collectUser(String userId);

    Result<Void> idIsValid(String userId);

    Result<Void> onCreateUserIsValid(String email);

    Result<User> onUpdateEmailIsValid(String userId, String newEmail);

    Result<User> onUpdatePasswordIsValid(String userId, String newPassword, String oldPassword);
}

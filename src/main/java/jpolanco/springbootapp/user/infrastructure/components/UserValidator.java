package jpolanco.springbootapp.user.infrastructure.components;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.components.adapter.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@RequiredArgsConstructor
@Component
public class UserValidator implements UserValidation {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean isInvalidUUID(String userId) {
        try {
            UUID.fromString(userId);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Result<User> basicValid(String userId) {
        if (isInvalidUUID(userId)) {
            return Result.failure(UserAppError.INVALID_ID_FORMAT);
        }
        var maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        return Result.success(user);
    }

    @Override
    public Result<User> collectUser(String userId) {
        if (idIsValid(userId).isFailure()) {
            return Result.failure(UserAppError.INVALID_ID_FORMAT);
        }
        var maybeUser = userRepository.findById(userId);
        return maybeUser.map(Result::success).orElseGet(() -> Result.failure(UserAppError.USER_NOT_FOUND));
    }

    @Override
    public Result<Void> idIsValid(String userId) {
        if (isInvalidUUID(userId)) {
            return Result.failure(UserAppError.INVALID_ID_FORMAT);
        }
        return Result.success();
    }

    @Override
    public Result<Void> onCreateUserIsValid(String email) {
        if (emailExist(email)) {
            return Result.failure(UserAppError.EMAIL_IS_ALREADY_IN_USE);
        }
        return Result.success();
    }

    @Override
    public Result<User> onUpdateEmailIsValid(String userId, String newEmail) {
        var validId = idIsValid(userId);
        if (validId.isFailure()) {
            return Result.failure(validId.getError());
        }
        var OptionalUser = userRepository.findById(userId);
        if (OptionalUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = OptionalUser.get();
        if (emailExist(newEmail) && !user.getEmail().equals(newEmail)) {
            return Result.failure(UserAppError.EMAIL_IS_ALREADY_IN_USE);
        }
        return Result.success();
    }

    @Override
    public Result<User> onUpdatePasswordIsValid(String userId, String newPassword, String oldPassword) {
        var validId = idIsValid(userId);
        if (validId.isFailure()) {
            return Result.failure(validId.getError());
        }
        if (isInvalidUUID(userId)) {
            return Result.failure(UserAppError.INVALID_ID_FORMAT);
        }
        if (newPassword.equals(oldPassword)) {
            return Result.failure(UserAppError.PASSWORDS_ARE_SAME);
        }
        var isValidId = idIsValid(userId);
        if (isValidId.isFailure()) {
            return Result.failure(isValidId.getError());
        }
        var maybeUser = collectUser(userId);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        if (user.isInactive()) {
            return Result.failure(UserAppError.USER_NOT_ACTIVE);
        }
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        if (!passwordEncoder.matches(oldPassword, user.getEncodedPassword())) {
            return Result.failure(UserAppError.OLD_PASSWORD_IS_INCORRECT);
        }
        return Result.success(user);
    }
}

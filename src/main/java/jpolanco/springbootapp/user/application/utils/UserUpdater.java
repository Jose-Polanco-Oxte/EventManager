package jpolanco.springbootapp.user.application.utils;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.QRProvider;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
public class UserUpdater {
    private final User user;
    private final PasswordEncoder passwordEncoder;
    private final QRProvider qrProvider;
    private final UserValidation userValidation;

    private Result<?> result = Result.success();

    private void check(Result<?> result) {
        if (result.isFailure() && this.result.isSuccess()) {
            this.result = result;
        }
    }

    public static UserUpdater updater(User user, PasswordEncoder passwordEncoder,
                                      QRProvider qrProvider, UserValidation userValidation) {
        return new UserUpdater(user, passwordEncoder, qrProvider, userValidation);
    }

    private boolean nullable(String value) {
        return value == null || value.isBlank();
    }

    public UserUpdater firstName(String firstName) {
        if (nullable(firstName)) return this;
        if (!user.getFirstName().equals(firstName)) {
            var result = user.changeFirstName(firstName);
            check(result);
        }
        return this;
    }

    public UserUpdater lastName(String lastName) {
        if (nullable(lastName)) return this;
        if (!user.getLastName().equals(lastName)) {
            var result = user.changeLastName(lastName);
            check(result);
        }
        return this;
    }

    public UserUpdater email(String email) {
        if (nullable(email)) return this;
        var valid = userValidation.onUpdateEmailIsValid(user.getId(), email);
        check(valid);
        if (!user.getEmail().equals(email)) {
            var result = user.changeEmail(email);
            check(result);
            var oldQRFileName = user.getQRFileName();
            var qrResult = user.newQRFileName();
            check(qrResult);
            qrProvider.delete(oldQRFileName);
            qrProvider.generate(user.getQRFileName(), email);
        }
        return this;
    }

    public UserUpdater password(String password) {
        if (nullable(password)) return this;
        var newEncodedPassword = passwordEncoder.encode(password);
        if (!user.getEncodedPassword().equals(newEncodedPassword)) {
            var result = user.changeEncodedPassword(newEncodedPassword);
            check(result);
        }
        return this;
    }

    public UserUpdater status(UserStatus status) {
        if (status == null) {
            return this;
        } else {
            user.changeStatus(status);
        }
        return this;
    }

    public UserUpdater roles(List<String> roles) {
        if (roles == null || roles.isEmpty()) return this;
        var result = user.changeAllRoles(roles);
        check(result);
        return this;
    }

    public Result<User> update() {
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(user);
    }
}

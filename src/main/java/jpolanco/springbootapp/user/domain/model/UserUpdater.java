package jpolanco.springbootapp.user.domain.model;

import jpolanco.springbootapp.shared.infrastructure.dto.response.Changes;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import lombok.RequiredArgsConstructor;


import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class UserUpdater {
    private final User user;

    private final Map<String, Error> errors = new HashMap<>();
    private final Map<String, Changes> changes = new HashMap<>();

    public static UserUpdater updater(User user) {
        return new UserUpdater(user);
    }

    private <T> void handle(Result<?> result, String field, T oldValue, T newValue) {
        if (result.isFailure()) {
            errors.put(field, result.getError());
        } else if (!equals(oldValue, newValue)) {
            changes.put(field, new Changes(field, String.valueOf(oldValue), String.valueOf(newValue)));
        }
    }

    private boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    // Métodos de modificación

    public UserUpdater firstName(String firstName) {
        return update("firstName", user.getFirstName(), () -> user.changeFirstName(firstName), user::getFirstName);
    }

    public UserUpdater lastName(String lastName) {
        return update("lastName", user.getLastName(), () -> user.changeLastName(lastName), user::getLastName);
    }

    public UserUpdater email(String email) {
        return update("email", user.getEmail(),
                () -> user.changeEmail(email),
                user::getEmail);
    }

    public UserUpdater password(String encodedPassword) {
        return update("encodedPassword", user.getEncodedPassword(),
                () -> user.changeEncodedPassword(encodedPassword),
                user::getEncodedPassword);
    }

    public UserUpdater status(String status) {
        if (status == null) return this;
        var old = user.getStatus().getValue();
        user.changeStatus(UserStatus.fromString(status));
        return updateSuccess("status", old, user.getStatus().getValue());
    }

    public UserUpdater reactivate() {
        return statusChange(user.getStatus(), user::reactivate, user::getStatus);
    }

    public UserUpdater suspend(String reason) {
        return statusChange(user.getStatus(), () -> user.suspend(reason), user::getStatus);
    }

    public UserUpdater deactivate(String reason) {
        return statusChange(user.getStatus(), () -> user.deactivate(reason), user::getStatus);
    }

    public UserUpdater roles(List<String> add, List<String> remove) {
        if (isEmpty(add) && isEmpty(remove)) return this;

        var old = user.getRoles();

        if (!isEmpty(remove)) user.removeRoles(remove);
        if (!isEmpty(add)) user.addRoles(add);

        return updateSuccess("roles", old, user.getRoles());
    }

    public UserUpdater generateNewQR() {
        var old = user.getQRFileName();
        user.newQRFileName();
        return updateSuccess("qrFileName", old, user.getQRFileName());
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    private UserUpdater updateSuccess(String field, Object oldVal, Object newVal) {
        handle(Result.success(), field, oldVal, newVal);
        return this;
    }

    private <T> UserUpdater update(String field, T oldValue, Supplier<Result<?>> action, Supplier<T> newValueSupplier) {
        var result = action.get();
        var newValue = newValueSupplier.get();
        handle(result, field, oldValue, newValue);
        return this;
    }

    private <T> UserUpdater statusChange(T oldValue, Runnable action, Supplier<T> newValueSupplier) {
        action.run();
        var newValue = newValueSupplier.get();
        handle(Result.success(), "status", oldValue, newValue);
        return this;
    }

    public UpdateReport update() {
        if (!errors.isEmpty()) return UpdateReport.failure(new ArrayList<>(errors.values()));
        return UpdateReport.success(new ArrayList<>(changes.values()), user.pullEvents());
    }
}
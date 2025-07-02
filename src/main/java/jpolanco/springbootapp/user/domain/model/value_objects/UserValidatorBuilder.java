package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.SuperResult;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class UserValidatorBuilder {
    private UserId userId;
    private FullName fullName;
    private Email email;
    private EncodedPassword encodedPassword;
    private Roles roles;
    private UserStatus status;
    private QRFileName qrFileName;
    private Instant createdAt;
    private EventNotification eventNotification;

    private final List<Error> errors = new ArrayList<>();

    public static UserValidatorBuilder builder() {
        return new UserValidatorBuilder();
    }

    private <T> T checker(Result<T> result) {
        if (result.isFailure()) {
            errors.add(result.getError());
            return null;
        } else {
            return result.getValue();
        }
    }

    private <S> S checker(SuperResult<S, Report> result) {
        if (result.isFailure()) {
            errors.addAll(result.getFailure().getErrors());
            return null;
        }
        return result.getSuccess();
    }

    public UserValidatorBuilder userId(UUID uuid) {
        var result = UserId.create(uuid);
        this.userId = checker(result);
        return this;
    }

    public UserValidatorBuilder fullName(String firstName, String lastName) {
        var result = FullName.create(firstName, lastName);
        this.fullName = checker(result);
        return this;
    }

    public UserValidatorBuilder email(String email) {
        var result = Email.create(email);
        this.email = checker(result);
        return this;
    }

    public UserValidatorBuilder encodedPassword(String encodedPassword) {
        var result = EncodedPassword.create(encodedPassword);
        this.encodedPassword = checker(result);
        return this;
    }

    public UserValidatorBuilder roles(List<String> roles) {
        var result = Roles.create(roles);
        this.roles = checker(result);
        return this;
    }

    public UserValidatorBuilder status(UserStatus status) {
        this.status = status;
        return this;
    }

    public UserValidatorBuilder qrFileName(String qrFileName) {
        var result = QRFileName.create(qrFileName);
        this.qrFileName = checker(result);
        return this;
    }

    public UserValidatorBuilder createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserValidatorBuilder addEvent(EventNotification event) {
        this.eventNotification = event;
        return this;
    }

    public SuperResult<User, Report> build() {
        if (!errors.isEmpty()) {
            return SuperResult.failure(Report.failure(errors));
        }

        var user = new User(
                userId,
                fullName,
                email,
                encodedPassword,
                roles,
                status,
                qrFileName,
                createdAt
        );
        user.recordEvent(eventNotification);
        return SuperResult.success(user);
    }
}

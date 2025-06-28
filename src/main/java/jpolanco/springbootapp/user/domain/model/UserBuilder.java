package jpolanco.springbootapp.user.domain.model;

import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.domain.model.value_objects.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class UserBuilder {
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

    private <T> T checker(Result<T> result) {
        System.out.println("Errors quantity: " + errors.size());
        if (result.isFailure()) {
            errors.add(result.getError());
            return null;
        } else {
            return result.getValue();
        }
    }

    public UserBuilder userId(String userId) {
        var result = UserId.create(userId);
        this.userId = checker(result);
        return this;
    }

    public UserBuilder fullName(String firstName, String lastName) {
        var result = FullName.create(firstName, lastName);
        this.fullName = checker(result);
        return this;
    }

    public UserBuilder email(String email) {
        var result = Email.create(email);
        this.email = checker(result);
        return this;
    }

    public UserBuilder encodedPassword(String encodedPassword) {
        var result = EncodedPassword.create(encodedPassword);
        this.encodedPassword = checker(result);
        return this;
    }

    public UserBuilder roles(List<String> roles) {
        var result = Roles.create(roles);
        this.roles = checker(result);
        return this;
    }

    public UserBuilder status(UserStatus status) {
        this.status = status;
        return this;
    }

    public UserBuilder qrFileName(String qrFileName) {
        var result = QRFileName.create(qrFileName);
        this.qrFileName = checker(result);
        return this;
    }

    public UserBuilder createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserBuilder addEvent(EventNotification event) {
        this.eventNotification = event;
        return this;
    }

    public Either<User, List<Error>> build() {
        if (!errors.isEmpty()) {
            return Either.right(List.copyOf(errors));
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
        return Either.left(user);
    }
}

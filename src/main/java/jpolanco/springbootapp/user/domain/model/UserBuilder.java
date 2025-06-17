package jpolanco.springbootapp.user.domain.model;

import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.*;

import java.time.Instant;
import java.util.Set;

public class UserBuilder {
    private Result<UserId> userId;
    private Result<FullName> fullName;
    private Result<Email> email;
    private Result<EncodedPassword> encodedPassword;
    private Result<Roles> roles;
    private UserStatus status;
    private Result<QRFileName> qrFileName;
    private Instant createdAt;
    private Result<Void> isValid = Result.success();
    private DomainEvent domainEvent;

    private void checker(Result<?> result) {
        if (result.isFailure() && !isValid.isFailure()) {
            isValid = Result.failure(result.getError());
        }
    }
    public UserBuilder userId(String userId) {
        Result<UserId> result = UserId.create(userId);
        checker(result);
        this.userId = result;
        return this;
    }

    public UserBuilder fullName(String firstName, String lastName) {
        Result<FullName> result = FullName.create(firstName, lastName);
        checker(result);
        this.fullName = result;
        return this;
    }

    public UserBuilder email(String email) {
        Result<Email> result = Email.create(email);
        checker(result);
        this.email = result;
        return this;
    }

    public UserBuilder encodedPassword(String encodedPassword) {
        Result<EncodedPassword> result = EncodedPassword.create(encodedPassword);
        checker(result);
        this.encodedPassword = result;
        return this;
    }

    public UserBuilder roles(Set<String> roles) {
        Result<Roles> result = Roles.create(roles);
        checker(result);
        this.roles = result;
        return this;
    }

    public UserBuilder status(UserStatus status) {
        this.status = status;
        return this;
    }

    public UserBuilder qrFileName(String qrFileName) {
        Result<QRFileName> result = QRFileName.create(qrFileName);
        checker(result);
        this.qrFileName = result;
        return this;
    }

    public UserBuilder createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserBuilder addEvent(DomainEvent event) {
        this.domainEvent = event;
        return this;
    }

    public Result<User> build() {
        if (isValid.isFailure()) {
            return Result.failure(isValid.getError());
        }

        var user = new User(
                userId.getValue(),
                fullName.getValue(),
                email.getValue(),
                encodedPassword.getValue(),
                roles.getValue(),
                status,
                qrFileName.getValue(),
                createdAt
        );
        user.recordEvent(domainEvent);
        return Result.success(user);
    }
}

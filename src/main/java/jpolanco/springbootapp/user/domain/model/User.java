package jpolanco.springbootapp.user.domain.model;


import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.domainevents.UserRegistered;
import jpolanco.springbootapp.user.domain.model.valueobjects.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class User {
    private final UserId userId;
    private FullName name;
    private Email email;
    private EncodedPassword encodedPassword;
    private Roles roles;
    private Status status;
    private QRFileName qrFileName;
    private final Instant createdAt;
    private List<DomainEvent> events = new ArrayList<>();

    private User(
            UserId userId,
            FullName name,
            Email email,
            EncodedPassword encodedPassword,
            Roles roles,
            Status status,
            QRFileName qrFileName,
            Instant createdAt
    ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = roles;
        this.status = status;
        this.qrFileName = qrFileName;
        this.createdAt = createdAt;
    }

    public static Result<User> create(
            String firstName,
            String lastName,
            String email,
            String encodedPassword
    ) {
        var userId = UUID.randomUUID().toString();
        return builder()
                .userId(userId)
                .fullName(firstName, lastName)
                .email(email)
                .encodedPassword(encodedPassword)
                .roles(Set.of(Role.create("USER").getValue()))
                .status("ACTIVE")
                .qrFileName(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .addEvent(new UserRegistered(userId, email))
                .build();
    }

    public static Result<User> createAdmin(
            String firstName,
            String lastName,
            String email,
            String encodedPassword
    ) {
        return builder()
                .userId(UUID.randomUUID().toString())
                .fullName(firstName, lastName)
                .email(email)
                .encodedPassword(encodedPassword)
                .roles(Set.of(Role.create("ADMIN").getValue()))
                .status("ACTIVE")
                .qrFileName(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .build();
    }

    public static Result<User> createOrganizer(
            String firstName,
            String lastName,
            String email,
            String encodedPassword
    ) {
        return builder()
                .userId(UUID.randomUUID().toString())
                .fullName(firstName, lastName)
                .email(email)
                .encodedPassword(encodedPassword)
                .roles(Set.of(Role.create("ORGANIZER").getValue()))
                .status("ACTIVE")
                .qrFileName(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .build();
    }

    public static Result<User> load(
            String userId,
            String firstName,
            String lastName,
            String email,
            String encodedPassword,
            Set<Role> roles,
            String status,
            String qrFileName,
            Instant createdAt
    ) {
        return builder()
                .userId(userId)
                .fullName(firstName, lastName)
                .email(email)
                .encodedPassword(encodedPassword)
                .roles(roles)
                .status(status)
                .qrFileName(qrFileName)
                .createdAt(createdAt)
                .build();
    }

    // Private builder constructor
    private static Builder builder() {
        return new Builder();
    }

    private static class Builder {
        private Result<UserId> userId;
        private Result<FullName> fullName;
        private Result<Email> email;
        private Result<EncodedPassword> encodedPassword;
        private Result<Roles> roles;
        private Result<Status> status;
        private Result<QRFileName> qrFileName;
        private Instant createdAt;
        private Result<Void> isValid = Result.success();
        private DomainEvent domainEvent;

        private void checker(Result<?> result) {
            if (result.isFailure() && !isValid.isFailure()) {
                isValid = Result.failure(result.getError());
            }
        }
        public Builder userId(String userId) {
            Result<UserId> result = UserId.create(userId);
            checker(result);
            this.userId = result;
            return this;
        }

        public Builder fullName(String firstName, String lastName) {
            Result<FullName> result = FullName.create(firstName, lastName);
            checker(result);
            this.fullName = result;
            return this;
        }

        public Builder email(String email) {
            Result<Email> result = Email.create(email);
            checker(result);
            this.email = result;
            return this;
        }

        public Builder encodedPassword(String encodedPassword) {
            Result<EncodedPassword> result = EncodedPassword.create(encodedPassword);
            checker(result);
            this.encodedPassword = result;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            Result<Roles> result = Roles.create(roles);
            checker(result);
            this.roles = result;
            return this;
        }

        public Builder status(String status) {
            Result<Status> result = Status.create(status);
            checker(result);
            this.status = result;
            return this;
        }

        public Builder qrFileName(String qrFileName) {
            Result<QRFileName> result = QRFileName.create(qrFileName);
            checker(result);
            this.qrFileName = result;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder addEvent(DomainEvent event) {
            this.domainEvent = event;
            return this;
        }

        private Result<User> build() {
            // Comprobamos el estado del result es valido
            if (isValid.isFailure()) {
                return Result.failure(isValid.getError());
            }

            var user = new User(
                    userId.getValue(),
                    fullName.getValue(),
                    email.getValue(),
                    encodedPassword.getValue(),
                    roles.getValue(),
                    status.getValue(),
                    qrFileName.getValue(),
                    createdAt
            );
            user.recordEvent(domainEvent);
            return Result.success(user);
        }
    }

    // Identificator
    public String getId() {
        return userId.getValue();
    }

    // Date of creation
    public Instant getCreatedAt() {
        return createdAt;
    }

    // Name domain
    public String getFirstName() {
        return name.getFirstName();
    }

    public String getLastName() {
        return name.getLastName();
    }

    public String getFullName() {
        return this.name.toString();
    }

    public Result<FullName> changeName(String newFirstName, String newLastName) {
        var result = FullName.create(newFirstName, newLastName);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.name = result.getValue();
        return result;
    }

    public Result<FullName> changeFirstName(String newFirstName) {
        var result = FullName.create(newFirstName, this.name.getLastName());
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.name = result.getValue();
        return result;
    }

    public Result<FullName> changeLastName(String newLastName) {
        var result = FullName.create(this.name.getFirstName(), newLastName);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.name = result.getValue();
        return result;
    }

    // Email domain
    public String getEmail() {
        return email.getValue();
    }

    public Result<Email> changeEmail(String newEmail) {
        var result = Email.create(newEmail);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.email = result.getValue();
        return result;
    }

    // EncodedPassword domain
    public Result<EncodedPassword> changePassword(String newEncodedPassword) {
        var result = EncodedPassword.create(newEncodedPassword);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.encodedPassword = result.getValue();
        return result;
    }

    public String getEncodedPassword() {
        return encodedPassword.getValue();
    }

    // Roles domain
    public Set<Role> getRoles() {
        return roles.getValues();
    }

    public Result<Roles> changeAllRoles(List<String> newRoles) {
        Result<Roles> result = Roles.create(newRoles.stream()
                .map(Role::create)
                .map(Result::getValue)
                .collect(Collectors.toSet()));

        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.roles = result.getValue();
        return result;
    }

    public Result<Role> addRole(String newRole) {
        var result = Role.create(newRole);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return this.roles.addValue(result.getValue());
    }

    public Result<Role> removeRole(String roleToRemove) {
        Result<Role> result = Role.create(roleToRemove);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return this.roles.removeValue(result.getValue());
    }

    public boolean hasRole(String roleToCheck) {
        Result<Role> result = Role.create(roleToCheck);
        if (result.isFailure()) {
            return false;
        }
        return this.roles.getValues().contains(result.getValue());
    }

    // Status domain
    public Result<Status> changeStatus(String newStatus) {
        var result = Status.create(newStatus);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.status = result.getValue();
        return result;
    }

    public boolean isActive() {
        return this.status.getValue().equals("ACTIVE");
    }

    public String getStatus() {
        return status.getValue();
    }

    public boolean equalsTo(User other) {
        return this.email.getValue().equals(other.email.getValue());
    }

    //QRFileName domain
    public String getQRFileName() {
        return qrFileName.getValue();
    }

    public Result<QRFileName> newQRFileName() {
        Result<QRFileName> result = QRFileName.create(UUID.randomUUID().toString());
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.qrFileName = result.getValue();
        return result;
    }

    public List<DomainEvent> pullEvents() {
        return this.events;
    }

    private void recordEvent(DomainEvent event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }
}
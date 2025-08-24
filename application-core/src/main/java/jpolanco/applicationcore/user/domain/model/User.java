package jpolanco.applicationcore.user.domain.model;

import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.CollectionResult;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.events.*;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

@Getter
public class User {
    private final UserId userId;
    private final Instant createdAt;
    private FullName name;
    private Email email;
    private EncodedPassword encodedPassword;
    private Roles roles;
    private UserStatus status;
    private final List<EventNotification> events = new ArrayList<>();

    // All-args constructor
    public User(
            UserId userId,
            FullName name,
            Email email,
            EncodedPassword encodedPassword,
            Roles roles,
            UserStatus status,
            Instant createdAt
    ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = roles;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Factory method to create a new User
    public static Result<User, DomainError> create(
            String firstName,
            String lastName,
            String email,
            String encodedPassword,
            List<String> roles
    ) {
        UUID uuid = UUID.randomUUID();
        // Validate and create each value object (appending errors if any)
        return UserId.create(uuid)
                .flatMap(userId -> Email.create(email)
                        .flatMap(email_ -> EncodedPassword.create(encodedPassword)
                                .flatMap(encodedPassword_ -> FullName.create(firstName, lastName)
                                        .flatMap(fullName -> Roles.create(roles)
                                                .map(roles_ -> {
                                                    User user = new User(
                                                            userId,
                                                            fullName,
                                                            email_,
                                                            encodedPassword_,
                                                            roles_,
                                                            UserStatus.ACTIVE,
                                                            Instant.now());
                                                    user.recordEvent(new UserRegistered(
                                                            userId.getUUID(),
                                                            email_.getValue()
                                                    ));
                                                    return user;
                                                })))));
    }

    // Factory method to reconstitute a User from persisted data
    public static User fromPersisted(
            Long userId,
            UUID uuid,
            String firstName,
            String lastName,
            String email,
            String encodedPassword,
            Set<UserRoles> roles,
            UserStatus status,
            Instant createdAt
    ) {
        return new User(
                UserId.loadUnchecked(userId, uuid),
                FullName.loadUnchecked(firstName, lastName),
                Email.loadUnchecked(email),
                EncodedPassword.loadUnchecked(encodedPassword),
                Roles.loadUnchecked(roles),
                status,
                createdAt
        );
    }

    // Name domain
    public Result<Void, DomainError> changeFirstName(String firstName) {
        Result<FullName, DomainError> result = FullName.create(firstName, this.name.getLastName());
        if (result.isFailure()) return result.toVoid();
        if (getName().equals(result.getValue())) return Result.success();
        this.name = result.getValue();
        return Result.success();
    }

    public Result<Void, DomainError> changeLastName(String lastName) {
        Result<FullName, DomainError> result = FullName.create(this.name.getFirstName(), lastName);
        if (result.isFailure()) return result.toVoid();
        if (getName().equals(result.getValue())) return Result.success();
        this.name = result.getValue();
        return Result.success();
    }

    public Result<Void, DomainError> changeEmail(String email) {
        Result<Email, DomainError> result = Email.create(email);
        if (result.isFailure()) return result.toVoid();
        if (getEmail().equals(result.getValue())) return Result.success();
        Email newEmail = result.getValue();
        recordEvent(new UserEmailChanged(
                getUserId().getUUID(),
                this.email.getValue(),
                newEmail.getValue()
        ));
        this.email = newEmail;
        return Result.success();
    }

    // EncodedPassword domain
    public Result<Void, DomainError> changeEncodedPassword(String encodedPassword) {
        Result<EncodedPassword, DomainError> result = EncodedPassword.create(encodedPassword);
        if (result.isFailure()) return result.toVoid();
        if (getEncodedPassword().equals(result.getValue())) return Result.success();
        EncodedPassword newEncodedPassword = result.getValue();
        recordEvent(new UserPasswordChanged(getUserId().getUUID(), getEmail().getValue()));
        this.encodedPassword = newEncodedPassword;
        return Result.success();
    }

    public Result<Void, DomainError> addRoles(List<String> roles) {
        CollectionResult<UserRoles, Roles> result = this.roles.addAll(roles);
        if (result.isFailure()) return Result.failure(result.errors());
        List<String> addedRoles = result.added().stream()
                .map(UserRoles::getValue)
                .toList();
        recordEvent(new UserAddedRoles(
                getUserId().getUUID(),
                getEmail().getValue(),
                this.roles.get(),
                result.updated().get(),
                addedRoles
        ));
        this.roles = result.updated();
        return Result.success();
    }

    public Result<Void, DomainError> removeRoles(List<String> roles) {
        CollectionResult<UserRoles, Roles> result = this.roles.removeAll(roles);
        if (result.isFailure()) return Result.failure(result.errors());
        List<String> removedRoles = result.removed().stream()
                .map(UserRoles::getValue)
                .toList();
        recordEvent(new UserRemovedRoles(
                getUserId().getUUID(),
                getEmail().getValue(),
                this.roles.get(),
                result.updated().get(),
                removedRoles
        ));
        this.roles = result.updated();
        return Result.success();
    }

    public boolean hasRole(String role) {
        return this.roles.get().contains(role);
    }

    // Status domain
    public void changeStatus(UserStatus status, String reason) {
        if (status == null || this.status.equals(status)) {
            return; // No change needed
        }
        if (reason == null || reason.isBlank()) {
            reason = "No reason provided";
        }
        switch (status) {
            case ACTIVE -> reactivate();
            case INACTIVE -> deactivate(reason);
            case SUSPENDED -> suspend(reason);
        }
    }

    public boolean isActive() {
        return this.status.equals(UserStatus.ACTIVE);
    }

    public boolean isInactive() {
        return this.status.equals(UserStatus.INACTIVE);
    }

    public boolean isSuspended() {
        return this.status.equals(UserStatus.SUSPENDED);
    }

    public void suspend(String reason) {
        if (isSuspended()) {
            return;
        }
        recordEvent(new UserSuspended(
                getUserId().getUUID(),
                reason == null ? "" : reason,
                getStatus(),
                UserStatus.SUSPENDED
        ));
        this.status = UserStatus.SUSPENDED;
    }

    public void deactivate(String reason) {
        if (isInactive() || isSuspended()) {
            return;
        }
        recordEvent(new UserDeactivated(
                getUserId().getUUID(),
                reason == null ? "" : reason,
                getStatus(),
                UserStatus.INACTIVE
        ));
        this.status = UserStatus.INACTIVE;
    }

    public void reactivate() {
        if (isActive()) {
            return;
        }
        recordEvent(new UserReactivated(
                getUserId().getUUID(),
                getStatus(),
                UserStatus.ACTIVE
        ));
        this.status = UserStatus.ACTIVE;
    }

    public UserStatus getStatus() {
        return status;
    }

    // Roles domain
    public boolean isAdmin() {
        return this.roles.get().contains(UserRoles.ADMIN.getValue());
    }

    public boolean isUser() {
        return this.roles.get().contains(UserRoles.USER.getValue());
    }

    public boolean isOrganizer() {
        return this.roles.get().contains(UserRoles.ORGANIZER.getValue());
    }

    //QRFileName domain
    public String getQR() {
        return this.email.getValue();
    }

    public List<EventNotification> pullEvents() {
        List<EventNotification> pulledEvents = List.copyOf(this.events);
        clearEvents();
        return pulledEvents;
    }

    // Event sourcing methods
    public User copyEventsFrom(User other) {
        Optional.ofNullable(other.pullEvents())
                .orElse(Collections.emptyList())
                .forEach(this::recordEvent);
        return this;
    }

    public User replaceEventsFrom(User other) {
        clearEvents();
        return copyEventsFrom(other);
    }

    public void recordEvent(EventNotification event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", word=" + name +
                ", email=" + email +
                ", encodedPassword=" + encodedPassword +
                ", roles=" + roles +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId.getUUID(), user.userId.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
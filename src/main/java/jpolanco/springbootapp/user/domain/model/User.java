package jpolanco.springbootapp.user.domain.model;


import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Either;
import jpolanco.springbootapp.user.domain.domain_events.*;
import jpolanco.springbootapp.user.domain.model.value_objects.*;

import java.time.Instant;
import java.util.*;


public class User {
    private final UserId userId;
    private FullName name;
    private Email email;
    private EncodedPassword encodedPassword;
    private Roles roles;
    private UserStatus status;
    private QRFileName qrFileName;
    private final Instant createdAt;
    private List<EventNotification> events = new ArrayList<>();

    protected User(
            UserId userId,
            FullName name,
            Email email,
            EncodedPassword encodedPassword,
            Roles roles,
            UserStatus status,
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

    public static Either<User, List<Error>> create(
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
                .roles(List.of("USER"))
                .status(UserStatus.ACTIVE)
                .qrFileName(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .addEvent(new UserRegistered(userId, email))
                .build();
    }

    public static Either<User, List<Error>> of(
            String id,
            String firstName,
            String lastName,
            String email,
            String encodedPassword
    ) {
        return builder()
                .userId(id)
                .fullName(firstName, lastName)
                .email(email)
                .encodedPassword(encodedPassword)
                .roles(List.of("USER"))
                .status(UserStatus.ACTIVE)
                .qrFileName(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .addEvent(new UserRegistered(id, email))
                .build();
    }

    public static Either<User, List<Error>> load(
            String userId,
            String firstName,
            String lastName,
            String email,
            String encodedPassword,
            List<String> roles,
            UserStatus status,
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
    private static UserBuilder builder() {
        return new UserBuilder();
    }

    // Identificator
    public String getId() {
        return userId.getValue();
    }

    // Date invoke creation
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

    public Result<FullName> changeFirstName(String firstName) {
        var result = FullName.create(firstName, this.name.getLastName());
        if (result.isFailure()) {
            return result;
        }
        this.name = result.getValue();
        return result;
    }

    public Result<FullName> changeLastName(String lastName) {
        var result = FullName.create(this.name.getFirstName(), lastName);
        if (result.isFailure()) {
            return result;
        }
        this.name = result.getValue();
        return result;
    }

    // Email domain
    public String getEmail() {
        return email.getValue();
    }

    public Result<Email> changeEmail(String email) {
        if (getEmail().equals(email)) return Email.create(getEmail());
        var oldEmail = getEmail();
        var result = Email.create(email);
        if (result.isFailure()) {
            return result;
        }
        this.email = result.getValue();
        recordEvent(new UserEmailChanged(
                getId(),
                oldEmail,
                getEmail()
        ));
        return result;
    }

    // EncodedPassword domain
    public Result<EncodedPassword> changeEncodedPassword(String encodedPassword) {
        var result = EncodedPassword.create(encodedPassword);
        if (result.isFailure()) {
            return result;
        }
        this.encodedPassword = result.getValue();
        recordEvent(new UserPasswordChanged(getId(), getEmail()));
        return result;
    }

    public String getEncodedPassword() {
        return encodedPassword.getValue();
    }

    // Roles domain
    public List<String> getRoles() {
        return roles.get();
    }

    public void addRoles(List<String> roles) {
        var addedRoles = this.roles.addAll(roles);
        if (!addedRoles.isEmpty()) {
            recordEvent(new UserAddedRoles(getId(), getEmail(), addedRoles));
        }
    }

    public void removeRoles(List<String> roles) {
        var removedRoles = this.roles.removeAll(roles);
        if (!removedRoles.isEmpty()) {
            recordEvent(new UserRemovedRoles(getId(), getEmail(), removedRoles));
        }
    }

    public boolean hasRole(String role) {
        return this.roles.get().contains(role);
    }

    // Status domain
    public void changeStatus(UserStatus status) {
        if (status == null || this.status.equals(status)) {
            return; // No change needed
        }
        switch (status) {
            case ACTIVE -> reactivate();
            case INACTIVE -> deactivate("User deactivated by admin");
            case SUSPENDED -> suspend("User suspended by admin");
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
        this.status = UserStatus.SUSPENDED;
        recordEvent(new UserSuspended(
                getId(),
                reason == null ? " " : reason
        ));
    }

    public void deactivate(String reason) {
        if (isInactive() || isSuspended()) {
            return;
        }
        this.status = UserStatus.INACTIVE;
        recordEvent(new UserDeactivated(
                getId(),
                reason == null ? " " : reason
        ));
    }

    public void reactivate() {
        if (isActive()) {
            return;
        }
        this.status = UserStatus.ACTIVE;
        recordEvent(new UserReactivated(
                getId()
        ));
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
    public String getQRFileName() {
        return qrFileName.getValue();
    }

    public void newQRFileName() {
        Result<QRFileName> result = QRFileName.create(UUID.randomUUID().toString());
        this.qrFileName = result.getValue();
    }

    public List<EventNotification> pullEvents() {
        return this.events;
    }

    public User copyEventsFrom(User other) {
        Optional.ofNullable(other.pullEvents())
                .orElse(Collections.emptyList())
                .forEach(this::recordEvent);
        return this;
    }

    public User replaceEventsFrom(User other) {
        this.events.clear();
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
                ", name=" + name +
                ", email=" + email +
                ", encodedPassword=" + encodedPassword +
                ", roles=" + roles +
                ", status=" + status +
                ", qrFileName=" + qrFileName +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
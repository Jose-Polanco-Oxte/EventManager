package jpolanco.springbootapp.user.domain.model;


import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
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
                .roles(List.of("USER"))
                .status(UserStatus.ACTIVE)
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
                .roles(List.of("ADMIN"))
                .status(UserStatus.ACTIVE)
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
                .roles(List.of("ORGANIZER"))
                .status(UserStatus.ACTIVE)
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

    public Result<FullName> changeFirstName(String firstName) {
        var result = FullName.create(firstName, this.name.getLastName());
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.name = result.getValue();
        return result;
    }

    public Result<FullName> changeLastName(String lastName) {
        var result = FullName.create(this.name.getFirstName(), lastName);
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

    public Result<Email> changeEmail(String email) {
        var oldEmail = getEmail();
        var result = Email.create(email);
        if (result.isFailure()) {
            return Result.failure(result.getError());
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
            return Result.failure(result.getError());
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

    public Result<Roles> changeAllRoles(List<String> roles) {
        var result = Roles.create(roles);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.roles = result.getValue();
        return result;
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
        switch (status) {
            case ACTIVE -> {
                if (this.status.equals(UserStatus.INACTIVE) || this.status.equals(UserStatus.SUSPENDED)) {
                    reactivate();
                }
            }
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
        this.status = UserStatus.SUSPENDED;
        recordEvent(new UserSuspended(
                getId(),
                reason
        ));
    }

    public void deactivate(String reason) {
        this.status = UserStatus.INACTIVE;
        recordEvent(new UserDeactivated(
                getId(),
                reason
        ));
    }

    public void reactivate() {
        this.status = UserStatus.ACTIVE;
        recordEvent(new UserReactivated(
                getId()
        ));
    }

    public String getStatus() {
        return status.getValue();
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

    public List<EventNotification> pullEvents() {
        return this.events;
    }

    public void recordEvent(EventNotification event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }
}
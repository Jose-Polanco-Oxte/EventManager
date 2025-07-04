package jpolanco.springbootapp.user.domain.model.valueobjects;


import jpolanco.springbootapp.shared.utils.results.Report;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.utils.results.SuperResult;
import jpolanco.springbootapp.user.domain.domainevents.*;
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

    public static SuperResult<User, Report> create(
            String firstName,
            String lastName,
            String email,
            String encodedPassword
    ) {
        var userId = UUID.randomUUID();
        return UserValidatorBuilder.builder()
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

    public static SuperResult<User, Report> of(
            Long userId,
            UUID uuid,
            String firstName,
            String lastName,
            String email,
            String encodedPassword
    ) {
        var user = new User(
                UserId.loadUnchecked(userId, uuid),
                FullName.loadUnchecked(firstName, lastName),
                Email.loadUnchecked(email),
                EncodedPassword.loadUnchecked(encodedPassword),
                Roles.loadUnchecked(Set.of(UserRoles.USER)),
                UserStatus.ACTIVE,
                QRFileName.loadUnchecked(UUID.randomUUID().toString()),
                Instant.now());
        user.recordEvent(new UserRegistered(user.getUUID(), user.getEmail()));
        return SuperResult.success(user);
    }

    public static User fromPersisted(
            Long userId,
            UUID uuid,
            String firstName,
            String lastName,
            String email,
            String encodedPassword,
            Set<UserRoles> roles,
            UserStatus status,
            String qrFileName,
            Instant createdAt
    ) {
        return new User(
                UserId.loadUnchecked(userId, uuid),
                FullName.loadUnchecked(firstName, lastName),
                Email.loadUnchecked(email),
                EncodedPassword.loadUnchecked(encodedPassword),
                Roles.loadUnchecked(roles),
                status,
                QRFileName.loadUnchecked(qrFileName),
                createdAt
        );
    }

    // Identificator
    public Long getId() {
        return userId.getId();
    }

    public UUID getUUID() {
        return userId.getUUID();
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
            return Result.failure(result.getFailure().getErrors().getFirst());
        }
        this.name = result.getSuccess();
        return Result.success(result.getSuccess());
    }

    public Result<FullName> changeLastName(String lastName) {
        var result = FullName.create(this.name.getFirstName(), lastName);
        if (result.isFailure()) {
            return Result.failure(result.getFailure().getErrors().getFirst());
        }
        this.name = result.getSuccess();
        return Result.success(result.getSuccess());
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
                getUUID(),
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
        recordEvent(new UserPasswordChanged(getUUID(), getEmail()));
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
            recordEvent(new UserAddedRoles(getUUID(), getEmail(), addedRoles));
        }
    }

    public void removeRoles(List<String> roles) {
        var removedRoles = this.roles.removeAll(roles);
        if (!removedRoles.isEmpty()) {
            recordEvent(new UserRemovedRoles(getUUID(), getEmail(), removedRoles));
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
                getUUID(),
                reason == null ? " " : reason
        ));
    }

    public void deactivate(String reason) {
        if (isInactive() || isSuspended()) {
            return;
        }
        this.status = UserStatus.INACTIVE;
        recordEvent(new UserDeactivated(
                getUUID(),
                reason == null ? " " : reason
        ));
    }

    public void reactivate() {
        if (isActive()) {
            return;
        }
        this.status = UserStatus.ACTIVE;
        recordEvent(new UserReactivated(
                getUUID()
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
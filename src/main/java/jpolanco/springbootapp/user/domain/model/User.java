package jpolanco.springbootapp.user.domain.model;


import jpolanco.springbootapp.shared.domain.DomainEvent;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.domainevents.UserRegistered;
import jpolanco.springbootapp.user.domain.model.valueobjects.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


public class User {
    private final UserId userId;
    private FullName name;
    private Email email;
    private EncodedPassword encodedPassword;
    private Roles roles;
    private UserStatus status;
    private QRFileName qrFileName;
    private final Instant createdAt;
    private List<DomainEvent> events = new ArrayList<>();

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
                .roles(Set.of("USER"))
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
                .roles(Set.of("ADMIN"))
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
                .roles(Set.of("ORGANIZER"))
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
            Set<String> roles,
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

    public Result<FullName> changeName(String firstName, String lastName) {
        var result = FullName.create(firstName, lastName);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.name = result.getValue();
        return result;
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
        var result = Email.create(email);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.email = result.getValue();
        return result;
    }

    // EncodedPassword domain
    public Result<EncodedPassword> changePassword(String encodedPassword) {
        var result = EncodedPassword.create(encodedPassword);
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
    public Set<String> getRoles() {
        return roles.getValues();
    }

    public Result<Roles> changeAllRoles(List<String> roles) {
        var set = new HashSet<>(roles);
        var result = Roles.create(set);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.roles = result.getValue();
        return result;
    }

    public void addRole(String role) {
        this.roles.addValue(role);
    }

    public void removeRole(String role) {
        this.roles.removeValue(role);
    }

    public boolean hasRole(String role) {
        return this.roles.getValues().contains(role);
    }

    // Status domain
    public void changeStatus(UserStatus status) {
        this.status = status;
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

    public List<DomainEvent> pullEvents() {
        return this.events;
    }

    public void recordEvent(DomainEvent event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        this.events.clear();
    }
}
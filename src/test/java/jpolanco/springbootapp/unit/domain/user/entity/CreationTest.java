package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserRoles;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("User Entity Creation Tests")
public class CreationTest {
    private User userEntity;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";
    private List<String> roles = List.of(UserRoles.USER.getValue());
    private UserStatus status = UserStatus.ACTIVE;
    private Instant createdAt;

    @BeforeEach
    void setUp() {
        // Initialize the UserEntity instance before each test
        userEntity = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        ).getSuccess();
        createdAt = Instant.now();
        userEntity.clearEvents();
    }

    @Test
    @DisplayName("Should create a UserEntity with valid parameters")
    void shouldCreateUserEntityWithValidParameters() {
        // Values #1
        String firstName = "John";
        String lastName = "Doe";
        String email = "test@gmail.com";
        String encodedPassword = "encodedPassword123";
        var startTime = Instant.now();
        var maybeUser = User.create(firstName, lastName, email, encodedPassword);
        System.out.println("Time taken to create user: " + (Instant.now().toEpochMilli() - startTime.toEpochMilli()) + " ms");
        assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters: ");
        assertEquals(1, maybeUser.getSuccess().pullEvents().size(), "One event should be recorded for user creation");
        for (var event : maybeUser.getSuccess().pullEvents()) {
            assertEquals("UserRegistered", event.getClass().getSimpleName(), "Event should be UserRegistered");
        }

        // Values #2
        firstName = "Jane Rose";
        lastName = "Smith ";
        email = "jaen.rol@gmail.com";
        encodedPassword = "encoded_452";
        maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters");
        assertEquals(1, maybeUser.getSuccess().pullEvents().size(), "One event should be recorded for user creation");
        for (var event : maybeUser.getSuccess().pullEvents()) {
            assertEquals("UserRegistered", event.getClass().getSimpleName(), "Event should be UserRegistered");
        }

        // Values #3
        firstName = "Alice";
        lastName = "Johnson Mcnally";
        email = "content_tew.F@hotmail.com";
        encodedPassword = "encoded_1234";
        maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters:");
        assertEquals(1, maybeUser.getSuccess().pullEvents().size(), "One event should be recorded for user creation");
        for (var event : maybeUser.getSuccess().pullEvents()) {
            assertEquals("UserRegistered", event.getClass().getSimpleName(), "Event should be UserRegistered");
        }
    }

    @Test
    @DisplayName("Should fail to create UserEntity with invalid parameters")
    void shouldFailToCreateUserEntityWithInvalidParameters() {
        // Invalid email
        String firstName = "John";
        String lastName = "Doe";
        String email = "invalid-email";
        String encodedPassword = "encodedPassword123";
        var maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isFailure(), "User creation should fail with invalid email");

        // Empty values
        firstName = "";
        lastName = "";
        email = "";
        encodedPassword = "";
        maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isFailure(), "User creation should fail with empty values");

        // Null values
        firstName = null;
        lastName = null;
        email = null;
        encodedPassword = null;
        var startTime = Instant.now();
        maybeUser = User.create(firstName, lastName, email, encodedPassword);
        System.out.println("Time taken to create user with null values: " + (Instant.now().toEpochMilli() - startTime.toEpochMilli()) + " ms");
        assertTrue(maybeUser.isFailure(), "User creation should fail with null values");
    }

    @Test
    @DisplayName("Should print all errors when creating UserEntity with invalid parameters")
    void shouldPrintAllErrorsWhenCreatingUserEntityWithInvalidParameters() {
        // Invalid email
        String firstName = "John";
        String lastName = "Doe";
        String email = "invalid-email";
        String encodedPassword = "encodedPassword123";
        var maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isFailure(), "User creation should fail with empty values");
        assertEquals(1, maybeUser.getFailure().getErrors().size(), "One event should be recorded for user creation");

        // Empty values
        firstName = "";
        lastName = "";
        email = "";
        encodedPassword = "validEncodedPassword123";
        maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isFailure(), "User creation should fail with empty values");
        assertEquals(3, maybeUser.getFailure().getErrors().size(), "One event should be recorded for user creation");

        // Null values
        firstName = null;
        lastName = null;
        email = null;
        encodedPassword = null;
        maybeUser = User.create(firstName, lastName, email, encodedPassword);
        assertTrue(maybeUser.isFailure(), "User creation should fail with null values");
        assertTrue(maybeUser.isFailure(), "User creation should fail with null values");
        assertEquals(4, maybeUser.getFailure().getErrors().size(), "One event should be recorded for user creation");
    }

    @Test
    @DisplayName("Validation invoke UserEntity properties")
    void shouldValidateUserEntityProperties() {
        // Not null entity
        assertNotNull(userEntity, "User entity should not be null");

        // Check if the user entity has the correct properties
        assertNotNull(userEntity.getFirstName(), "Last name should not be null");
        assertEquals(firstName, userEntity.getFirstName(), "First name should match");
        assertNotNull(userEntity.getLastName(), "Last name should not be null");
        assertEquals(lastName, userEntity.getLastName(), "Last name should match");
        assertNotNull(userEntity.getEmail(), "Email should not be null");
        assertEquals(email, userEntity.getEmail(), "Email should match");
        assertNotNull(userEntity.getEncodedPassword(), "Encoded password should not be null");
        assertEquals(encodedPassword, userEntity.getEncodedPassword(), "Encoded password should match");

        // Check roles
        assertNotNull(userEntity.getRoles(), "Roles should not be null");
        assertFalse(userEntity.getRoles().isEmpty(), "Roles should not be empty");
        assertEquals(roles, userEntity.getRoles(), "Roles should match");

        // Check status
        assertNotNull(userEntity.getStatus(), "Status should not be null");
        assertEquals(status, userEntity.getStatus(), "Status should match");

        // Check QR file name
        assertNotNull(userEntity.getQRFileName(), "Created at should not be null");
        var checkQRUUID = UUID.fromString(userEntity.getQRFileName());
        assertNotNull(checkQRUUID, "QR file name should be a valid UUID");

        // Check created at
        assertNotNull(userEntity.getCreatedAt(), "Created at should not be null");
        assertEquals(createdAt, userEntity.getCreatedAt(), "Created at should match the initialized value");

        // Check ID
        assertNotNull(userEntity.getId(), "ID should not be null");
        var  checkIDUUID = UUID.fromString(userEntity.getId());
        assertNotNull(checkIDUUID, "ID should be a valid UUID");
    }

    @Test
    @DisplayName("Should create UserEntity with default values")
    void shouldCreateUserEntityWithDefaultValues() {
        assertTrue(userEntity.isUser());
        assertFalse(userEntity.isAdmin());
        assertFalse(userEntity.isOrganizer());
        assertTrue(userEntity.isActive(), "Default status should be ACTIVE");
        assertFalse(userEntity.isInactive(), "Default status should not be INACTIVE");
        assertFalse(userEntity.isSuspended(), "Default status should be SUSPENDED");
        assertEquals(1, userEntity.getRoles().size(), "Roles should match");
        assertEquals(userEntity.getCreatedAt(), createdAt, "Created at should match the initialized value");
    }

    @Nested
    @DisplayName("Time Performance Tests")
    class TimePerformanceTests {
        @Test
        @DisplayName("Should create UserEntity within acceptable time limits")
        void shouldCreateUserEntityWithinAcceptableTimeLimits() {
            // Measure time taken to create a user entity
            var startTime = Instant.now();
            var maybeUser = User.create(firstName, lastName, email, encodedPassword);
            var endTime = Instant.now();
            long duration = endTime.toEpochMilli() - startTime.toEpochMilli();
            System.out.println("Time taken to create user: " + duration + " ms");
            assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters");
            assertTrue(duration < 30, "User creation should be fast enough (less than 30 ms)");
        }
    }
}

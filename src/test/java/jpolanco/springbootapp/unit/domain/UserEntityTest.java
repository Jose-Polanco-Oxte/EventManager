package jpolanco.springbootapp.unit.domain;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.domain_events.UserAddedRoles;
import jpolanco.springbootapp.user.domain.domain_events.UserRemovedRoles;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserRoles;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserEntity Tests")
public class UserEntityTest {

    /**
     * This class contains tests for the UserEntity class.
     * The tests will cover various aspects of the UserEntity,
     * including its properties, methods, and interactions
     * with other components in the application.
     */

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
        ).getValue();
        createdAt = Instant.now();
        userEntity.clearEvents();
    }

    @Nested
    @DisplayName("Creation tests")
    class CreationTests {

        @Test
        @DisplayName("Should create a UserEntity with valid parameters")
        void shouldCreateUserEntityWithValidParameters() {
            // Values #1
            String firstName = "John";
            String lastName = "Doe";
            String email = "test@gmail.com";
            String encodedPassword = "encodedPassword123";
            Result<User> maybeUser = User.create(firstName, lastName, email, encodedPassword);
            assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters: " + maybeUser.getError());
            assertEquals(1, maybeUser.getValue().pullEvents().size(), "One event should be recorded for user creation");
            for (var event : maybeUser.getValue().pullEvents()) {
                assertEquals("UserRegistered", event.getClass().getSimpleName(), "Event should be UserRegistered");
            }

            // Values #2
            firstName = "Jane Rose";
            lastName = "Smith ";
            email = "jaen.rol@gmail.com";
            encodedPassword = "encoded_452";
            maybeUser = User.create(firstName, lastName, email, encodedPassword);
            assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters: " + maybeUser.getError());
            assertEquals(1, maybeUser.getValue().pullEvents().size(), "One event should be recorded for user creation");
            for (var event : maybeUser.getValue().pullEvents()) {
                assertEquals("UserRegistered", event.getClass().getSimpleName(), "Event should be UserRegistered");
            }

            // Values #3
            firstName = "Alice";
            lastName = "Johnson Mcnally";
            email = "content_tew.F@hotmail.com";
            encodedPassword = "encoded_1234";
            maybeUser = User.create(firstName, lastName, email, encodedPassword);
            assertTrue(maybeUser.isSuccess(), "User creation should succeed with valid parameters: " + maybeUser.getError());
            assertEquals(1, maybeUser.getValue().pullEvents().size(), "One event should be recorded for user creation");
            for (var event : maybeUser.getValue().pullEvents()) {
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
            Result<User> maybeUser = User.create(firstName, lastName, email, encodedPassword);
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
            maybeUser = User.create(firstName, lastName, email, encodedPassword);
            assertTrue(maybeUser.isFailure(), "User creation should fail with null values");
        }

        @Test
        @DisplayName("Validation of UserEntity properties")
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
    }

    @Nested
    @DisplayName("Name interaction tests")
    class NameInteractionTests {

        @Test
        @DisplayName("Should change first name successfully")
        void shouldChangeFirstNameSuccessfully() {
            String newFirstName = "Michael";
            var result = userEntity.changeFirstName(newFirstName);
            assertTrue(result.isSuccess(), "Changing first name should succeed");
            assertEquals(newFirstName, userEntity.getFirstName(), "First name should be updated");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }

        @Test
        @DisplayName("Should fail to change first name with empty value")
        void shouldFailToChangeFirstNameWithEmptyValue() {
            String newFirstName = "";
            var result = userEntity.changeFirstName(newFirstName);
            assertTrue(result.isFailure(), "Changing first name with empty value should fail");
            assertEquals(firstName, userEntity.getFirstName(), "First name should remain unchanged");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }

        @Test
        @DisplayName("Should change last name successfully")
        void shouldChangeLastNameSuccessfully() {
            String newLastName = "Smith";
            var result = userEntity.changeLastName(newLastName);
            assertTrue(result.isSuccess(), "Changing last name should succeed");
            assertEquals(newLastName, userEntity.getLastName(), "Last name should be updated");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }

        @Test
        @DisplayName("Should fail to change last name with empty value")
        void shouldFailToChangeLastNameWithEmptyValue() {
            String newLastName = "";
            var result = userEntity.changeLastName(newLastName);
            assertTrue(result.isFailure(), "Changing last name with empty value should fail");
            assertEquals(lastName, userEntity.getLastName(), "Last name should remain unchanged");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }
    }

    @Nested
    @DisplayName("Email interaction tests")
    class EmailInteractionTests {

        @Test
        @DisplayName("Should change email successfully")
        void shouldChangeEmailSuccessfully() {
            String newEmail = "papu@gmail.com";
            var result = userEntity.changeEmail(newEmail);
            assertTrue(result.isSuccess(), "Changing email should succeed");
            assertEquals(newEmail, userEntity.getEmail(), "Email should be updated");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
            for (var event : userEntity.pullEvents()) {
                assertEquals("UserEmailChanged", event.getClass().getSimpleName(), "Event should be UserEmailChanged");
            }
        }

        @Test
        @DisplayName("Should fail to change email with invalid format")
        void shouldFailToChangeEmailWithInvalidFormat() {
            String newEmail = "invalid-email-format";
            var result = userEntity.changeEmail(newEmail);
            assertTrue(result.isFailure(), "Changing email with invalid format should fail");
            assertEquals(email, userEntity.getEmail(), "Email should remain unchanged");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }
    }

    @Nested
    @DisplayName("Encoded password interaction tests")
    class EncodedPasswordInteractionTests {

        @Test
        @DisplayName("Should change encoded password successfully")
        void shouldChangeEncodedPasswordSuccessfully() {
            String newEncodedPassword = "newEncodedPassword123";
            var result = userEntity.changeEncodedPassword(newEncodedPassword);
            assertTrue(result.isSuccess(), "Changing encoded password should succeed");
            assertEquals(newEncodedPassword, userEntity.getEncodedPassword(), "Encoded password should be updated");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            for (var event : userEntity.pullEvents()) {
                assertEquals("UserPasswordChanged", event.getClass().getSimpleName(), "Event should be UserPasswordChanged");
            }
        }

        @Test
        @DisplayName("Should fail to change encoded password with empty value")
        void shouldFailToChangeEncodedPasswordWithEmptyValue() {
            String newEncodedPassword = "";
            var result = userEntity.changeEncodedPassword(newEncodedPassword);
            assertTrue(result.isFailure(), "Changing encoded password with empty value should fail");
            assertEquals(encodedPassword, userEntity.getEncodedPassword(), "Encoded password should remain unchanged");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }
    }

    @Nested
    @DisplayName("Roles interaction tests")
    class RolesInteractionTests {

        @BeforeEach
        void setUpRoles() {
            userEntity = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
            ).getValue();
            userEntity.clearEvents();
        }

        @Test
        @DisplayName("Should add roles successfully")
        void shouldAddRolesSuccessfully() {
            List<String> newRoles = List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue());
            userEntity.addRoles(newRoles);
            assertFalse(userEntity.getRoles().isEmpty(), "Roles should not be empty after adding");
            assertTrue(userEntity.isUser(), "User should be user");
            assertTrue(userEntity.isAdmin(), "User should have admin role");
            assertTrue(userEntity.isOrganizer(), "User should have organizer role");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            for (var event : userEntity.pullEvents()) {
                var added = (UserAddedRoles) event;
                assertEquals(Set.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()), new HashSet<>(added.getRoles()), "Roles should match the added roles");
                assertEquals("UserAddedRoles", event.getClass().getSimpleName(), "Event should be UserAddedRoles");
            }
        }

        @Test
        @DisplayName("Should ignore invalid roles in addRoles")
        void shouldIgnoreInvalidRolesInAddRoles() {
            List<String> invalidRoles = Arrays.asList("", UserRoles.ADMIN.getValue(), null);
            userEntity.addRoles(invalidRoles);
            assertFalse(userEntity.getRoles().isEmpty(), "Roles should not be empty after adding");
            assertTrue(userEntity.isUser(), "User should have user role");
            assertTrue(userEntity.isAdmin(), "User should have admin role");
            assertFalse(userEntity.isOrganizer(), "User should have organizer role");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            assertEquals(1, userEntity.pullEvents().size(), "Only one UserAddedRoles event should be recorded");
            for (var event : userEntity.pullEvents()) {
                var added = (UserAddedRoles) event;
                assertEquals(Set.of(UserRoles.ADMIN.getValue()), new HashSet<>(added.getRoles()), "Only valid roles should be added");
                assertEquals("UserAddedRoles", event.getClass().getSimpleName(), "Event should be UserAddedRoles");
            }
        }

        @Test
        @DisplayName("Should not add invalid roles")
        void shouldNotAddInvalidRoles() {
            List<String> invalidRoles = Arrays.asList("", "NON_EXISTENT_ROLE", null);
            userEntity.addRoles(invalidRoles);
            assertTrue(userEntity.isUser(), "User should have user role");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
        }

        @Test
        @DisplayName("Should remove roles successfully")
        void shouldRemoveRolesSuccessfully() {
            userEntity.addRoles(List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()));
            userEntity.clearEvents();
            List<String> rolesToRemove = List.of(UserRoles.ORGANIZER.getValue());
            userEntity.removeRoles(rolesToRemove);
            assertTrue(userEntity.isUser(), "User should have user role");
            assertTrue(userEntity.isAdmin(), "User should have admin role");
            assertFalse(userEntity.isOrganizer(), "User should not have organizer role");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            assertEquals(1, userEntity.pullEvents().size(), "Only one UserRemovedRoles event should be recorded");
            for (var event : userEntity.pullEvents()) {
                var removed = (UserRemovedRoles) event;
                assertEquals(Set.of(UserRoles.ORGANIZER.getValue()), new HashSet<>(removed.getRoles()), "Roles should match the removed roles");
                assertEquals("UserRemovedRoles", event.getClass().getSimpleName(), "Event should be UserRemovedRoles");
            }
        }

        @Test
        @DisplayName("Should ignore invalid roles in removeRoles")
        void shouldIgnoreInvalidRolesInRemoveRoles() {
            userEntity.addRoles(List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()));
            userEntity.clearEvents();
            List<String> invalidRolesToRemove = Arrays.asList("", "NON_EXISTENT_ROLE", null, UserRoles.ADMIN.getValue());
            userEntity.removeRoles(invalidRolesToRemove);
            assertTrue(userEntity.isUser(), "User should have user role");
            assertTrue(userEntity.isOrganizer(), "User should have organizer role");
            assertFalse(userEntity.isAdmin(), "User should not have admin role");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            assertEquals(1, userEntity.pullEvents().size(), "Only one UserRemovedRoles event should be recorded");
            for (var event : userEntity.pullEvents()) {
                var removed = (UserRemovedRoles) event;
                assertEquals(Set.of(UserRoles.ADMIN.getValue()), new HashSet<>(removed.getRoles()), "No valid roles should be removed");
                assertEquals("UserRemovedRoles", event.getClass().getSimpleName(), "Event should be UserRemovedRoles");
            }
        }

        @Test
        @DisplayName("Should not remove invalid roles")
        void shouldNotRemoveInvalidRoles() {
            userEntity.addRoles(List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()));
            userEntity.clearEvents();
            List<String> invalidRolesToRemove = Arrays.asList("", "NON_EXISTENT_ROLE", null);
            userEntity.removeRoles(invalidRolesToRemove);
            assertTrue(userEntity.isUser(), "User should have user role");
            assertTrue(userEntity.isAdmin(), "User should have admin role");
            assertTrue(userEntity.isOrganizer(), "User should have organizer role");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }

        @Test
        @DisplayName("Should not remove user role")
        void shouldNotRemoveUserRole() {
            userEntity.addRoles(List.of(UserRoles.ADMIN.getValue()));
            userEntity.clearEvents();
            List<String> rolesToRemove = List.of(UserRoles.USER.getValue());
            userEntity.removeRoles(rolesToRemove);
            assertTrue(userEntity.isUser(), "User role should not be removed");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }

        @Test
        @DisplayName("Should not remove user role when removing all roles")
        void shouldNotRemoveUserRoleWhenRemovingAllRoles() {
            userEntity.addRoles(List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()));
            userEntity.clearEvents();
            List<String> rolesToRemove = List.of(UserRoles.ADMIN.getValue(), UserRoles.USER.getValue(), UserRoles.ORGANIZER.getValue());
            userEntity.removeRoles(rolesToRemove);
            assertTrue(userEntity.isUser(), "User role should remain after removing all other roles");
            assertFalse(userEntity.isAdmin(), "User should not have admin role");
            assertFalse(userEntity.isOrganizer(), "User should not have organizer role");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
            assertEquals(1, userEntity.pullEvents().size(), "Only one UserRemovedRoles event should be recorded");
            for (var event : userEntity.pullEvents()) {
                var removed = (UserRemovedRoles) event;
                assertEquals(Set.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()), new HashSet<>(removed.getRoles()), "Roles should match the removed roles");
                assertEquals("UserRemovedRoles", event.getClass().getSimpleName(), "Event should be UserRemovedRoles");
            }
        }

        @Test
        @DisplayName("Should not remove all roles")
        void shouldNotRemoveAllRoles() {
            userEntity.removeRoles(List.of(UserRoles.USER.getValue()));
            assertTrue(userEntity.isUser(), "User role should not be removed");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }
    }

    @Nested
    @DisplayName("Status interaction tests")
    class StatusInteractionTests {

        @Test
        @DisplayName("Suspend user status successfully")
        void shouldChangeStatusSuccessfully() {
            userEntity.suspend("User suspended by admin");
            assertEquals(UserStatus.SUSPENDED, userEntity.getStatus(), "Status should be updated");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            for (var event : userEntity.pullEvents()) {
                assertEquals("UserSuspended", event.getClass().getSimpleName(), "Event should be UserStatusChanged");
            }
        }

        @Test
        @DisplayName("Deactivate user status successfully")
        void shouldDeactivateStatusSuccessfully() {
            userEntity.deactivate("User deactivated by admin");
            assertEquals(UserStatus.INACTIVE, userEntity.getStatus(), "Status should be updated");
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            for (var event : userEntity.pullEvents()) {
                assertEquals("UserDeactivated", event.getClass().getSimpleName(), "Event should be UserStatusChanged");
            }
        }

        @Test
        @DisplayName("Reactivate user status successfully")
        void shouldReactivateStatusSuccessfully() {
            userEntity.reactivate();
            assertEquals(UserStatus.ACTIVE, userEntity.getStatus(), "Status should be updated");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            for (var event : userEntity.pullEvents()) {
                assertEquals("UserReactivated", event.getClass().getSimpleName(), "Event should be UserStatusChanged");
            }
        }

        @Test
        @DisplayName("Should fail to change status with null value")
        void shouldFailToChangeStatusWithNullValue() {
            userEntity.changeStatus(null);
            assertEquals(status, userEntity.getStatus(), "Status should remain unchanged");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }

        @Test
        @DisplayName("Should ignore changing status to the same value")
        void shouldIgnoreChangingStatusToTheSameValue() {
            userEntity.changeStatus(status);
            assertEquals(status, userEntity.getStatus(), "Status should remain unchanged");
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
        }
    }

    @Nested
    @DisplayName("QR File Name interaction tests")
    class QRFileNameInteractionTests {

        @Test
        @DisplayName("Should change QR file name successfully")
        void shouldChangeQRFileNameSuccessfully() {
            var oldQRFileName = userEntity.getQRFileName();
            userEntity.newQRFileName();
            assertNotEquals(oldQRFileName, userEntity.getQRFileName(), "QR file name should be updated");
            var checkQRUUID = UUID.fromString(userEntity.getQRFileName());
            assertNotNull(checkQRUUID, "QR file name should be a valid UUID");
        }
    }

    @Nested
    @DisplayName("Domain events methods tests")
    class DomainEventsMethodsTests {

        @Test
        @DisplayName("Should record events correctly")
        void shouldRecordEventsCorrectly() {
            userEntity.recordEvent(new UserAddedRoles(userEntity.getId(), userEntity.getEmail(), roles));
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
            for (var event : userEntity.pullEvents()) {
                assertEquals("UserAddedRoles", event.getClass().getSimpleName(), "Event should be UserAddedRoles");
                var added = (UserAddedRoles) event;
                assertEquals(userEntity.getId(), added.getUserId(), "User ID should match");
                assertEquals(userEntity.getEmail(), added.getEmail(), "Email should match");
                assertEquals(roles, added.getRoles(), "Roles should match");
            }
        }

        @Test
        @DisplayName("Should not record null events")
        void shouldNotRecordNullEvents() {
            userEntity.recordEvent(null);
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty when recording null event");
        }

        @Test
        @DisplayName("Should clear events correctly")
        void shouldClearEventsCorrectly() {
            userEntity.recordEvent(new UserAddedRoles(userEntity.getId(), userEntity.getEmail(), roles));
            assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty before clearing");
            userEntity.clearEvents();
            assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty after clearing");
        }
    }
}
package jpolanco.springbootapp.unit.domain;

import jpolanco.springbootapp.shared.domain.DomainError;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import jpolanco.springbootapp.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserUpdaterTest {

    private User user;
    private UserUpdater userUpdater;

    @BeforeEach
    public void setUp() {
        // Initialize a sample user for testing
        user = User.create(
                "JohnDoe",
                "DOE",
                "johndoe@gmail.com",
                "password123"
        ).getValue();
        userUpdater = new UserUpdater(user);
    }

    @Test
    @DisplayName("Test updating user first name")
    public void testUpdateUserFirstName() {
        String newFirstName = "Jane";
        var report = userUpdater.firstName(newFirstName).update();
        assertEquals(newFirstName, user.getFirstName(), "First name should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid first name update");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user last name")
    public void testUpdateUserLastName() {
        String newLastName = "Smith";
        var report = userUpdater.lastName(newLastName).update();
        assertEquals(newLastName, user.getLastName(), "Last name should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid last name update");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user email")
    public void testUpdateUserEmail() {
        String newEmail = "newEmail@outlook.com";
        var report = userUpdater.email(newEmail).update();
        assertEquals(newEmail, user.getEmail(), "Email should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid email update");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user password")
    public void testUpdateUserPassword() {
        var encodedPassword = "encodedNewPassword"; // Simulate encoded password
        userUpdater.password(encodedPassword);
        var report = userUpdater.update();
        assertEquals(encodedPassword, user.getEncodedPassword(), "Password should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid password update");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user status")
    public void testUpdateUserStatus() {
        String newStatus = "SUSPENDED";
        var report = userUpdater.status(newStatus).update();
        assertEquals(newStatus, user.getStatus().toString(), "Status should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid status update");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");

        var reactivateReport = userUpdater.reactivate().update();
        assertTrue(user.isActive(), "User should be suspended");
        assertFalse(reactivateReport.isFailure(), "Report should not fail on reactivation");
        assertTrue(reactivateReport.isHasChanges(), "Report should contain changes on reactivation");

        var suspendReport = userUpdater.suspend("Maintenance").update();
        assertTrue(user.isSuspended(), "User should be suspended after suspend call");
        assertFalse(suspendReport.isFailure(), "Report should not fail on suspension");
        assertTrue(suspendReport.isHasChanges(), "Report should contain changes on suspension");

        var reactivateAgainReport = userUpdater.reactivate().update();
        assertTrue(user.isActive(), "User should be active after reactivation");
        assertFalse(reactivateAgainReport.isFailure(), "Report should not fail on reactivation");
        assertTrue(reactivateAgainReport.isHasChanges(), "Report should contain changes on reactivation");

        var deactivateReport = userUpdater.deactivate("None").update();
        assertTrue(user.isInactive(), "User should be inactive after deactivation");
        assertFalse(deactivateReport.isFailure(), "Report should not fail on deactivation");
        assertTrue(deactivateReport.isHasChanges(), "Report should contain changes on deactivation");
    }

    @Test
    @DisplayName("Test updating user roles")
    public void testUpdateUserRoles() {
        var addRoles = List.of("ADMIN", "ORGANIZER");
        var removeRoles = List.of("ADMIN");
        var report = userUpdater.roles(addRoles, removeRoles).update();
        assertTrue(user.getRoles().containsAll(addRoles), "User should have added roles");
        assertFalse(report.isFailure(), "Report should not fail on valid role update");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test generating new QR code")
    public void testGenerateNewQRCode() {
        String oldQRFileName = user.getQRFileName();
        var report = userUpdater.generateNewQR().update();
        assertNotEquals(oldQRFileName, user.getQRFileName(), "QR file name should be updated");
        assertFalse(report.isFailure(), "Report should not fail on QR code generation");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test verifying report errors")
    public void testReportErrors() {
        // Simulate an error in the user update process
        var report = userUpdater
                .firstName("f") // Invalid first name (too short)
                .email(null) // Invalid email (null)
                .lastName("d".repeat(200)) // Invalid last name (too long)
                .update();
        assertTrue(report.isFailure(), "Report should fail on invalid first name");
        assertFalse(report.isHasChanges(), "Report should not have changes on failure");
        assertFalse(report.getErrors().isEmpty(), "Report should contain errors");

        System.out.println("Errors in the report:");
        for (var error : report.getErrors()) {
            System.out.println(error.getField());
            System.out.println(error.getMessage());
            if (error instanceof DomainError e && e.getDetails() != null) {
                System.out.println(e.getDetails());
            }
            System.out.println("_".repeat(50));
        }
        System.out.println("Changes in the report:");
        for (var changes : report.getChanges()) {
            System.out.println(changes.fieldName());
            System.out.println(changes.oldValue());
            System.out.println(changes.newValue());
            System.out.println("_".repeat(50));
        }
    }

    @Test
    @DisplayName("Test updating the same field multiple times")
    public void testUpdateSameFieldMultipleTimes() {
        String firstName = "Alice";
        String lastName = "Johnson";
        String email = "jose@gmail.com";
        String password = "newPassword123";

        var report = userUpdater
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .firstName("Tony") // Same first name again
                .lastName("Mari") // Same last name again
                .email(email)
                .password(password)
                .update();
        assertFalse(report.isFailure(), "Report should not fail on valid updates");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
        assertEquals("Tony", user.getFirstName(), "First name should be updated to the last value");
        assertEquals("Mari", user.getLastName(), "Last name should be updated to the last value");
        assertEquals(email, user.getEmail(), "Email should be updated to the last value");
        assertEquals(password, user.getEncodedPassword(), "Password should be updated to the last value");
        assertEquals(4, report.getChanges().size(), "Report should contain changes for each field updated");
        for (var change : report.getChanges()) {
            assertNotNull(change.fieldName(), "Change field name should not be null");
            assertNotNull(change.oldValue(), "Change old value should not be null");
            assertNotNull(change.newValue(), "Change new value should not be null");
        }
    }

    @Test
    @DisplayName("Test report errors, only contains the lasts errors with multiple failed updates")
    public void testReportErrorsOnlyLasts() {
        String firstName = "f"; // Invalid first name (too short)
        String lastName = "d".repeat(200); // Invalid last name (too long)

        var report = userUpdater
                .firstName(firstName)
                .email(null)
                .lastName(lastName)
                .email("P")
                .update();
        assertTrue(report.isFailure(), "Report should fail on invalid updates");
        assertFalse(report.isHasChanges(), "Report should not have changes on failure");
        assertEquals(3, report.getErrors().size(), "Report should contain 3 errors");

        System.out.println("Errors in the report:");
        for (var error : report.getErrors()) {
            System.out.println(error.getField());
            System.out.println(error.getMessage());
            if (error instanceof DomainError e && e.getDetails() != null) {
                System.out.println(e.getDetails());
            }
            System.out.println("_".repeat(50));
        }
    }

    @Test
    @DisplayName("Test updating user with invalid data")
    public void testUpdateUserWithInvalidData() {
        String invalidEmail = "invalidEmail"; // Invalid email format
        String shortFirstName = "A"; // Too short first name
        String longLastName = "L".repeat(200); // Too long last name
        String invalidPassword = "srt"; // Invalid password (too short)

        var startTime = System.currentTimeMillis();
        var report = userUpdater
                .email(invalidEmail)
                .firstName(shortFirstName)
                .lastName(longLastName)
                .suspend(null)
                .password(invalidPassword)
                .email("pol@gmail.com") // Valid email to test error handling
                .update();
        System.out.println("Time taken for invalid update: " + (System.currentTimeMillis() - startTime) + " ms");

        assertTrue(report.isFailure(), "Report should fail on invalid data");
        assertFalse(report.isHasChanges(), "Report should not have changes on failure");
        assertFalse(report.getErrors().isEmpty(), "Report should contain errors for invalid updates");
        assertEquals(4, report.getErrors().size(), "Report should contain 4 errors for invalid updates");

        System.out.println("Errors in the report:");
        for (var error : report.getErrors()) {
            System.out.println(error.getField());
            System.out.println(error.getMessage());
            if (error instanceof DomainError e && e.getDetails() != null) {
                System.out.println(e.getDetails());
            }
            System.out.println("_".repeat(50));
        }
    }

    @Test
    @DisplayName("Test updating all fields at once")
    public void testUpdateAllFieldsAtOnce() {
        String newFirstName = "Alice";
        String newLastName = "Johnson";
        String newEmail = "newEmail@yahoo.com";
        String newPassword = "newPassword123";
        var startTime = System.currentTimeMillis();
        var report = userUpdater
                .firstName(newFirstName)
                .lastName(newLastName)
                .email(newEmail)
                .password(newPassword)
                .deactivate("User requested deactivation")
                .generateNewQR()
                .firstName(newFirstName)
                .firstName(newFirstName)
                .roles(List.of(), List.of())
                .update();

        System.out.println("Time taken for update: " + (System.currentTimeMillis() - startTime) + " ms");

        assertFalse(report.isFailure(), "Report should not fail on valid updates");
        assertTrue(report.isHasChanges(), "Report should contain changes to update");
        assertEquals(newFirstName, user.getFirstName(), "First name should be updated");
        assertEquals(newLastName, user.getLastName(), "Last name should be updated");
        assertEquals(newEmail, user.getEmail(), "Email should be updated");
        assertEquals(newPassword, user.getEncodedPassword(), "Password should be updated");
        assertEquals("INACTIVE", user.getStatus().toString(), "User status should be INACTIVE after deactivation");
        assertNotNull(user.getQRFileName(), "QR file name should be generated");
        assertEquals(6, report.getChanges().size(), "Report should contain changes for all fields updated");
        for (var change : report.getChanges()) {
            assertNotNull(change.fieldName(), "Change field name should not be null");
            assertNotNull(change.oldValue(), "Change old value should not be null");
            assertNotNull(change.newValue(), "Change new value should not be null");
        }
    }
}
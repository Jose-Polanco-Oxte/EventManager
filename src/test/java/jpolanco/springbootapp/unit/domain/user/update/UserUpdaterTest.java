package jpolanco.springbootapp.unit.domain.user.update;

import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserUpdater interaction tests")
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
        ).getSuccess();
        userUpdater = new UserUpdater(user);
    }

    @Test
    @DisplayName("Test updating user first name")
    public void testUpdateUserFirstName() {
        String newFirstName = "Jane";
        var report = userUpdater.firstName(newFirstName).update();
        assertEquals(newFirstName, user.getFirstName(), "First name should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid first name update");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user last name")
    public void testUpdateUserLastName() {
        String newLastName = "Smith";
        var report = userUpdater.lastName(newLastName).update();
        assertEquals(newLastName, user.getLastName(), "Last name should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid last name update");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user email")
    public void testUpdateUserEmail() {
        String newEmail = "newEmail@outlook.com";
        var report = userUpdater.email(newEmail).update();
        assertEquals(newEmail, user.getEmail(), "Email should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid email update");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user password")
    public void testUpdateUserPassword() {
        var encodedPassword = "encodedNewPassword"; // Simulate encoded password
        userUpdater.password(encodedPassword);
        var report = userUpdater.update();
        assertEquals(encodedPassword, user.getEncodedPassword(), "Password should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid password update");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test updating user status")
    public void testUpdateUserStatus() {
        String newStatus = "SUSPENDED";
        var report = userUpdater.status(newStatus).update();
        assertEquals(newStatus, user.getStatus().toString(), "Status should be updated");
        assertFalse(report.isFailure(), "Report should not fail on valid status update");
        assertTrue(report.hasChanges(), "Report should contain changes to update");

        var reactivateReport = userUpdater.reactivate().update();
        assertTrue(user.isActive(), "User should be suspended");
        assertFalse(reactivateReport.isFailure(), "Report should not fail on reactivation");
        assertTrue(reactivateReport.hasChanges(), "Report should contain changes on reactivation");

        var suspendReport = userUpdater.suspend("Maintenance").update();
        assertTrue(user.isSuspended(), "User should be suspended after suspend call");
        assertFalse(suspendReport.isFailure(), "Report should not fail on suspension");
        assertTrue(suspendReport.hasChanges(), "Report should contain changes on suspension");

        var reactivateAgainReport = userUpdater.reactivate().update();
        assertTrue(user.isActive(), "User should be active after reactivation");
        assertFalse(reactivateAgainReport.isFailure(), "Report should not fail on reactivation");
        assertTrue(reactivateAgainReport.hasChanges(), "Report should contain changes on reactivation");

        var deactivateReport = userUpdater.deactivate("None").update();
        assertTrue(user.isInactive(), "User should be inactive after deactivation");
        assertFalse(deactivateReport.isFailure(), "Report should not fail on deactivation");
        assertTrue(deactivateReport.hasChanges(), "Report should contain changes on deactivation");
    }

    @Test
    @DisplayName("Test updating user roles")
    public void testUpdateUserRoles() {
        var addRoles = List.of("ADMIN", "ORGANIZER");
        var removeRoles = List.of("ADMIN");
        var report = userUpdater.roles(addRoles, removeRoles).update();
        assertTrue(user.getRoles().containsAll(addRoles), "User should have added roles");
        assertFalse(report.isFailure(), "Report should not fail on valid role update");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Test generating new QR code")
    public void testGenerateNewQRCode() {
        String oldQRFileName = user.getQRFileName();
        var report = userUpdater.generateNewQR().update();
        assertNotEquals(oldQRFileName, user.getQRFileName(), "QR file name should be updated");
        assertFalse(report.isFailure(), "Report should not fail on QR code generation");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
    }

    @Test
    @DisplayName("Time performance for updating user")
    public void testUpdateUserPerformance() {
        String firstName = "Tony";
        String lastName = "Mari";
        String email = "new@gmail.com";
        String password = "newPassword123";
        var startTime = System.currentTimeMillis();
        var report = userUpdater
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .deactivate("User requested deactivation")
                .generateNewQR()
                .roles(List.of("ADMIN"), List.of("ORGANIZER"))
                .update();
        System.out.println("Time taken for update: " + (System.currentTimeMillis() - startTime) + " ms");
        assertFalse(report.isFailure(), "Report should not fail on valid updates");
        assertTrue(report.hasChanges(), "Report should contain changes to update");
        assertEquals(firstName, user.getFirstName(), "First name should be updated");
        assertEquals(lastName, user.getLastName(), "Last name should be updated");
        assertEquals(email, user.getEmail(), "Email should be updated");
        assertEquals(password, user.getEncodedPassword(), "Password should be updated");
        assertEquals("INACTIVE", user.getStatus().toString(), "User status should be INACTIVE after deactivation");
        assertNotNull(user.getQRFileName(), "QR file name should be generated");
        assertTrue(user.getRoles().contains("USER"), "User should have USER role");
        assertTrue(user.getRoles().contains("ADMIN"), "User should not have ADMIN role after removal");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("firstName")), "Report should contain first name change");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("lastName")), "Report should contain last name change");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("email")), "Report should contain email change");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("encodedPassword")), "Report should contain password change");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("status")), "Report should contain status change");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("qrFileName")), "Report should contain QR file name change");
        assertTrue(report.getChanges().stream().anyMatch(change -> change.field().equals("roles")), "Report should contain roles change");
    }
}
package jpolanco.springbootapp.unit.domain.user.update;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChangesTest {
    private User user;
    private UserUpdater userUpdater;

    @BeforeEach
    public void setUp() {
        // Initialize a sample userId for testing
        user = User.create(
                "JohnDoe",
                "DOE",
                "johndoe@gmail.com",
                "password123"
        ).getSuccess();
        userUpdater = new UserUpdater(user);
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
        assertTrue(report.hasChanges(), "Report should contain changes to update");
        assertEquals("Tony", user.getFirstName(), "First name should be updated to the last value");
        assertEquals("Mari", user.getLastName(), "Last name should be updated to the last value");
        assertEquals(email, user.getEmail(), "Email should be updated to the last value");
        assertEquals(password, user.getEncodedPassword(), "Password should be updated to the last value");
        assertEquals(4, report.getChanges().size(), "Report should contain changes for each field updated");
        for (var change : report.getChanges()) {
            assertNotNull(change.field(), "Change field name should not be null");
            assertNotNull(change.before(), "Change old value should not be null");
            assertNotNull(change.after(), "Change new value should not be null");
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
        assertTrue(report.hasChanges(), "Report should contain changes to update");
        assertEquals(newFirstName, user.getFirstName(), "First name should be updated");
        assertEquals(newLastName, user.getLastName(), "Last name should be updated");
        assertEquals(newEmail, user.getEmail(), "Email should be updated");
        assertEquals(newPassword, user.getEncodedPassword(), "Password should be updated");
        assertEquals("INACTIVE", user.getStatus().toString(), "User status should be INACTIVE after deactivation");
        assertNotNull(user.getQRFileName(), "QR file name should be generated");
        assertEquals(6, report.getChanges().size(), "Report should contain changes for all fields updated");
        for (var change : report.getChanges()) {
            assertNotNull(change.field(), "Change field name should not be null");
            assertNotNull(change.before(), "Change old value should not be null");
            assertNotNull(change.after(), "Change new value should not be null");
        }
    }

    @Nested
    @DisplayName("Time Complexity Tests")
    class TimeComplexityTests {
        @Test
        @DisplayName("Test time complexity for multiple updates")
        public void testTimeComplexityForMultipleUpdates() {
            String firstName = "Alice";
            String lastName = "Johnson";
            String email = "teste@gmail.com";
            String password = "newPassword123";
            var startTime = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                userUpdater
                        .firstName(firstName + "g".repeat(i % 10))
                        .lastName(lastName + "h".repeat(i % 10))
                        .email(i + email)
                        .password(password + i);
            }
            var report = userUpdater.update();
            System.out.println("Time taken for 1000 updates: " + (System.currentTimeMillis() - startTime) + " ms");
            assertTrue(System.currentTimeMillis() - startTime < 500, "Update should complete in reasonable time");
            assertFalse(report.isFailure(), "Report should not fail on valid updates" + report);
            assertTrue(report.hasChanges(), "Report should contain changes to update");
            assertEquals("Alice" + "g".repeat(999 % 10), user.getFirstName(), "First name should be updated to the last value");
            assertEquals("Johnson" + "h".repeat(999 % 10), user.getLastName(), "Last name should be updated to the last value");
            assertEquals("999teste@gmail.com", user.getEmail(), "Email should be updated to the last value");
            assertEquals("newPassword123999", user.getEncodedPassword(), "Password should be updated to the last value");
            assertEquals(4, report.getChanges().size(), "Report should contain changes for each field updated");
            for (var change : report.getChanges()) {
                assertNotNull(change.field(), "Change field name should not be null");
                assertNotNull(change.before(), "Change old value should not be null");
                assertNotNull(change.after(), "Change new value should not be null");
            }
        }
    }
}

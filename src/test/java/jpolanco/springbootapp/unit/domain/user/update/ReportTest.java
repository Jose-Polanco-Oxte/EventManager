package jpolanco.springbootapp.unit.domain.user.update;

import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.domain.model.UserUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Report interaction tests")
public class ReportTest {
    private UserUpdater userUpdater;

    @BeforeEach
    public void setUp() {
        userUpdater = new UserUpdater(User.create(
                "JohnDoe",
                "DOE",
                "johndoe@gmail.com",
                "password123"
        ).getSuccess());
    }

    @Test
    @DisplayName("Test verifying report errors")
    public void testReportErrors() {
        // Simulate an error in the userId update process
        var report = userUpdater
                .firstName("f") // Invalid first name (too short)
                .email(null) // Invalid email (null)
                .lastName("d".repeat(200)) // Invalid last name (too long)
                .update();
        assertTrue(report.isFailure(), "Report should fail on invalid first name");
        assertFalse(report.hasChanges(), "Report should not have changes on failure");
        assertFalse(report.getErrors().isEmpty(), "Report should contain errors");
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
        assertFalse(report.hasChanges(), "Report should not have changes on failure");
        assertEquals(3, report.getErrors().size(), "Report should contain 3 errors");
    }

    @Test
    @DisplayName("Test updating userId with invalid data")
    public void testUpdateUserWithInvalidData() {
        String invalidEmail = "invalidEmail"; // Invalid email format
        String shortFirstName = "A"; // Too short first name
        String longLastName = "L".repeat(200); // Too long last name
        String invalidPassword = "srt"; // Invalid password (too short)

        var report = userUpdater
                .email(invalidEmail)
                .firstName(shortFirstName)
                .lastName(longLastName)
                .suspend(null)
                .password(invalidPassword)
                .email("pol@gmail.com") // Valid email to test error handling
                .update();

        assertTrue(report.isFailure(), "Report should fail on invalid data");
        assertFalse(report.hasChanges(), "Report should not have changes on failure");
        assertFalse(report.getErrors().isEmpty(), "Report should contain errors for invalid updates");
        assertEquals(4, report.getErrors().size(), "Report should contain 4 errors for invalid updates");
    }

    @Test
    @DisplayName("Time for cartching errors in report")
    public void testTimeForCatchingErrorsInReport() {
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
                .update();
        System.out.println("Time taken for catching errors: " + (System.currentTimeMillis() - startTime) + " ms");

        assertTrue(report.isFailure(), "Report should fail on invalid data");
        assertFalse(report.hasChanges(), "Report should not have changes on failure");
        assertFalse(report.getErrors().isEmpty(), "Report should contain errors for invalid updates");
        assertEquals(4, report.getErrors().size(), "Report should contain 4 errors for invalid updates");
    }
}

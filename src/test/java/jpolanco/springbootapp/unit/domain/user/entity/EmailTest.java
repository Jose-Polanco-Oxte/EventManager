package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email interaction tests")
public class EmailTest {
    private User userEntity;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";

    @BeforeEach
    void setUp() {
        // Initialize the UserEntity instance before each test
        userEntity = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        ).getSuccess();
        userEntity.clearEvents();
    }

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

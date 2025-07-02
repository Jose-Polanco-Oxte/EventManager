package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.model.value_objects.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Name interaction tests")
public class NameTest {
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

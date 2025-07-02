package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Status interaction tests")
public class StatusTest {
    private User userEntity;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";
    private UserStatus status = UserStatus.ACTIVE;

    @BeforeEach
    void setUpRoles() {
        userEntity = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        ).getSuccess();
        userEntity.clearEvents();
    }

    @Test
    @DisplayName("Suspend userId status successfully")
    void shouldChangeStatusSuccessfully() {
        userEntity.suspend("User suspended by admin");
        assertEquals(UserStatus.SUSPENDED, userEntity.getStatus(), "Status should be updated");
        assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
        for (var event : userEntity.pullEvents()) {
            assertEquals("UserSuspended", event.getClass().getSimpleName(), "Event should be UserStatusChanged");
        }
    }

    @Test
    @DisplayName("Deactivate userId status successfully")
    void shouldDeactivateStatusSuccessfully() {
        userEntity.deactivate("User deactivated by admin");
        assertEquals(UserStatus.INACTIVE, userEntity.getStatus(), "Status should be updated");
        assertFalse(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
        for (var event : userEntity.pullEvents()) {
            assertEquals("UserDeactivated", event.getClass().getSimpleName(), "Event should be UserStatusChanged");
        }
    }

    @Test
    @DisplayName("Reactivate userId status successfully")
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

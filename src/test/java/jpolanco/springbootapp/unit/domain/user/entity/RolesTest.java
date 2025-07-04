package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.domainevents.UserAddedRoles;
import jpolanco.springbootapp.user.domain.domainevents.UserRemovedRoles;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Roles interaction tests")
public class RolesTest {
    private User userEntity;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";

    @BeforeEach
    void setUp() {
        userEntity = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        ).getSuccess();
        userEntity.clearEvents();
    }

    @Test
    @DisplayName("Should add roles successfully")
    void shouldAddRolesSuccessfully() {
        List<String> newRoles = List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue());
        userEntity.addRoles(newRoles);
        assertFalse(userEntity.getRoles().isEmpty(), "Roles should not be empty after adding");
        assertTrue(userEntity.isUser(), "User should be userId");
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
        assertTrue(userEntity.isUser(), "User should have userId role");
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
        assertTrue(userEntity.isUser(), "User should have userId role");
        assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should not be empty");
    }

    @Test
    @DisplayName("Should remove roles successfully")
    void shouldRemoveRolesSuccessfully() {
        userEntity.addRoles(List.of(UserRoles.ADMIN.getValue(), UserRoles.ORGANIZER.getValue()));
        userEntity.clearEvents();
        List<String> rolesToRemove = List.of(UserRoles.ORGANIZER.getValue());
        userEntity.removeRoles(rolesToRemove);
        assertTrue(userEntity.isUser(), "User should have userId role");
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
        assertTrue(userEntity.isUser(), "User should have userId role");
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
        assertTrue(userEntity.isUser(), "User should have userId role");
        assertTrue(userEntity.isAdmin(), "User should have admin role");
        assertTrue(userEntity.isOrganizer(), "User should have organizer role");
        assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
    }

    @Test
    @DisplayName("Should not remove userId role")
    void shouldNotRemoveUserRole() {
        userEntity.addRoles(List.of(UserRoles.ADMIN.getValue()));
        userEntity.clearEvents();
        List<String> rolesToRemove = List.of(UserRoles.USER.getValue());
        userEntity.removeRoles(rolesToRemove);
        assertTrue(userEntity.isUser(), "User role should not be removed");
        assertTrue(userEntity.pullEvents().isEmpty(), "Pull events should be empty");
    }

    @Test
    @DisplayName("Should not remove userId role when removing all roles")
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

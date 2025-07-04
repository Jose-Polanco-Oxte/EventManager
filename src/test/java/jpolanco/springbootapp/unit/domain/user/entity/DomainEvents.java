package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.domainevents.UserAddedRoles;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain Events interaction tests")
public class DomainEvents {
    private User user;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";
    private List<String> roles = List.of(UserRoles.USER.getValue());

    @BeforeEach
    void setUp() {
        // Initialize the UserEntity instance before each test
        user = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        ).getSuccess();
        user.clearEvents();
    }

    @Test
    @DisplayName("Should record events correctly")
    void shouldRecordEventsCorrectly() {
        user.recordEvent(new UserAddedRoles(user.getUUID(), user.getEmail(), roles));
        assertFalse(user.pullEvents().isEmpty(), "Pull events should not be empty");
        for (var event : user.pullEvents()) {
            assertEquals("UserAddedRoles", event.getClass().getSimpleName(), "Event should be UserAddedRoles");
            var added = (UserAddedRoles) event;
            assertEquals(user.getUUID(), added.getUserId(), "User ID should match");
            assertEquals(user.getEmail(), added.getEmail(), "Email should match");
            assertEquals(roles, added.getRoles(), "Roles should match");
        }
    }

    @Test
    @DisplayName("Should not record null events")
    void shouldNotRecordNullEvents() {
        user.recordEvent(null);
        assertTrue(user.pullEvents().isEmpty(), "Pull events should be empty when recording null event");
    }

    @Test
    @DisplayName("Should clear events correctly")
    void shouldClearEventsCorrectly() {
        user.recordEvent(new UserAddedRoles(user.getUUID(), user.getEmail(), roles));
        assertFalse(user.pullEvents().isEmpty(), "Pull events should not be empty before clearing");
        user.clearEvents();
        assertTrue(user.pullEvents().isEmpty(), "Pull events should be empty after clearing");
    }

    @Nested
    @DisplayName("Time performance tests")
    class TimePerformanceTests {
        @Test
        @DisplayName("Should record and pull events within acceptable time")
        void shouldRecordAndPullEventsWithinAcceptableTime() {
            long startTime = System.nanoTime();
            user.recordEvent(new UserAddedRoles(user.getUUID(), user.getEmail(), roles));
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            assertTrue(duration < 1000000, "Recording event should take less than 1 millisecond");

            startTime = System.nanoTime();
            var events = user.pullEvents();
            endTime = System.nanoTime();
            duration = endTime - startTime;
            assertTrue(duration < 1000000, "Pulling events should take less than 1 millisecond");
        }
    }
}

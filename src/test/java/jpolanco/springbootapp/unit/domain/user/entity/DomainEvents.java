package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.domain_events.UserAddedRoles;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.domain.model.value_objects.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain Events interaction tests")
public class DomainEvents {
    private User userEntity;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@gmail.com";
    private String encodedPassword = "52mfasih5234_23nF(#1hre";
    private List<String> roles = List.of(UserRoles.USER.getValue());

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

    @Nested
    @DisplayName("Time performance tests")
    class TimePerformanceTests {
        @Test
        @DisplayName("Should record and pull events within acceptable time")
        void shouldRecordAndPullEventsWithinAcceptableTime() {
            long startTime = System.nanoTime();
            userEntity.recordEvent(new UserAddedRoles(userEntity.getId(), userEntity.getEmail(), roles));
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            assertTrue(duration < 1000000, "Recording event should take less than 1 millisecond");

            startTime = System.nanoTime();
            var events = userEntity.pullEvents();
            endTime = System.nanoTime();
            duration = endTime - startTime;
            assertTrue(duration < 1000000, "Pulling events should take less than 1 millisecond");
        }
    }
}

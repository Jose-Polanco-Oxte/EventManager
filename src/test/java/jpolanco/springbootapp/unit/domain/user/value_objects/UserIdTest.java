package jpolanco.springbootapp.unit.domain.user.value_objects;

import jpolanco.springbootapp.user.domain.model.valueobjects.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserId Value Object Tests")
public class UserIdTest {
    @Test
    @DisplayName("Should create UserId with valid UUID")
    void shouldCreateUserIdWithValidUUID() {
        var uuid = UUID.randomUUID();
        var result = UserId.create(uuid);
        assertTrue(result.isSuccess(), "Expected success for valid UUID: " + uuid);
        assertEquals(uuid, result.getValue().getUUID());
        assertNotNull(result.getValue().getUUID(), "Expected UUID to be not null");
    }

    @Test
    @DisplayName("Should fail to create UserId with null UUID")
    void shouldFailToCreateUserIdWithNullUUID() {
        var result = UserId.create(null);
        assertTrue(result.isFailure(), "Expected failure for null UUID");
    }

    @Test
    @DisplayName("Two UserId objects with same value should be equal")
    void shouldBeEqualForSameUserIdValues() {
        var randomUUID = UUID.randomUUID();
        var userIdOne = UserId.create(randomUUID).getValue();
        var userIdTwo = UserId.create(randomUUID).getValue();
        assertEquals(userIdOne, userIdTwo, "Expected both UserId objects to be equal");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        var randomUUID = UUID.randomUUID();
        var userId = UserId.create(randomUUID).getValue();
        assertEquals(userId, userId, "Expected UserId to be equal to itself");
    }
}

package jpolanco.springbootapp.unit.domain.user.value_objects;

import jpolanco.springbootapp.user.domain.model.value_objects.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserId Value Object Tests")
public class UserIdTest {
    @Test
    @DisplayName("Should create UserId with valid UUID")
    void shouldCreateUserIdWithValidUUID() {
        String uuid = UUID.randomUUID().toString();
        var result = UserId.create(uuid);
        assertTrue(result.isSuccess(), "Expected success for valid UUID: " + uuid);
        assertEquals(uuid, result.getValue().getValue());
        var test = UUID.fromString(result.getValue().getValue());
        assertNotNull(test);
    }

    @Test
    @DisplayName("Should fail to create UserId with null UUID")
    void shouldFailToCreateUserIdWithNullUUID() {
        var result = UserId.create(null);
        assertTrue(result.isFailure(), "Expected failure for null UUID");

        var result2 = UserId.create("awfgawn 24_ty");
        assertTrue(result2.isFailure(), "Expected failure for invalid UUID format");
    }

    @Test
    @DisplayName("Should fail to create UserId with invalid UUID format")
    void shouldFailToCreateUserIdWithInvalidUUID() {
        List<String> invalidUUIDs = Arrays.asList(
                "",                     // Empty string
                " ",                    // Whitespace
                "invalid-uuid",        // Invalid UUID format
                "12345",                // Too short
                "12345678901234567890",// Too long
                "not-a-uuid-format"     // Completely invalid format
        );

        for (String uuid : invalidUUIDs) {
            var result = UserId.create(uuid);
            assertTrue(result.isFailure(), "Expected failure for invalid UUID: " + uuid);
        }
    }

    @Test
    @DisplayName("Two UserId objects with same value should be equal")
    void shouldBeEqualForSameUserIdValues() {
        var randomUUID = UUID.randomUUID().toString();
        var userIdOne = UserId.create(randomUUID).getValue();
        var userIdTwo = UserId.create(randomUUID).getValue();
        assertEquals(userIdOne, userIdTwo, "Expected both UserId objects to be equal");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        var randomUUID = UUID.randomUUID().toString();
        var userId = UserId.create(randomUUID).getValue();
        assertEquals(userId, userId, "Expected UserId to be equal to itself");
    }
}

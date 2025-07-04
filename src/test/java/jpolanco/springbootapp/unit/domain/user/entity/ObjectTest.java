package jpolanco.springbootapp.unit.domain.user.entity;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Object hashing, equality and toString tests")
public class ObjectTest {
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
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        assertEquals(userEntity, userEntity, "User entity should be equal to itself");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        assertNotEquals(null, userEntity, "User entity should not be equal to null");
    }

    @Test
    @DisplayName("Should not be equal to different class type")
    void shouldNotBeEqualToDifferentClassType() {
        assertNotEquals(new Object(), userEntity, "User entity should not be equal to different class type");
    }

    @Test
    @DisplayName("Should have same hash code for equal objects")
    void shouldHaveSameHashCodeForEqualObjects() {
        UUID testId = UUID.randomUUID();
        Long testIdLong = 2L;
        User user = User.of(testIdLong, testId, firstName, lastName, email, encodedPassword).getSuccess();
        User anotherUser = User.of(testIdLong, testId, firstName, lastName, email, encodedPassword).getSuccess();
        assertEquals(user.hashCode(), anotherUser.hashCode(), "Hash codes invoke equal userId entities should match");
    }

    @Test
    @DisplayName("Two UserEntities with the same ID should be equal")
    void twoUserEntitiesWithSameIdShouldBeEqual() {
        UUID testId = UUID.randomUUID();
        Long testIdLong = 2L;
        User user1 = User.of(testIdLong, testId, firstName, lastName, email, encodedPassword).getSuccess();
        User user2 = User.of(testIdLong, testId, firstName, lastName, email, encodedPassword).getSuccess();
        assertEquals(user1, user2, "Two UserEntities with the same ID should be equal");
    }

    @Test
    @DisplayName("Two UserEntities with different IDs should not be equal")
    void twoUserEntitiesWithDifferentIdsShouldNotBeEqual() {
        User user1 = User.of(4L, UUID.randomUUID(), firstName, lastName, email, encodedPassword).getSuccess();
        User user2 = User.of(1L, UUID.randomUUID(), firstName, lastName, email, encodedPassword).getSuccess();
        assertNotEquals(user1, user2, "Two UserEntities with different IDs should not be equal");
    }
}

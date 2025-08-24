package jpolanco.applicationcore;


import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AppendErrorsTest {

    @Test
    @DisplayName("Test appending errors to a Result and finally creating a user if all validations pass")
    public void testAppendErrors() {

        // simulate constructing a valid user
        Result<User, DomainError> resultSuccess =
                UserId.create(UUID.randomUUID())
                        .flatMap(userId -> FullName.create("John", "Doe")
                                .flatMap(fullName -> Email.create("example@gmail.com")
                                        .flatMap(email -> EncodedPassword.create("Pepe24123")
                                                .flatMap(encodedPassword -> Roles.create(List.of("USER"))
                                                        .map(roles -> new User(
                                                                userId,
                                                                fullName,
                                                                email,
                                                                encodedPassword,
                                                                roles,
                                                                UserStatus.ACTIVE,
                                                                Instant.now()
                                                        ))))));

        assertTrue(resultSuccess.isSuccess(), "Expected result to be successful");

        assertNotNull(resultSuccess.getValue(), "Expected user to be created successfully");

        User user = resultSuccess.getValue();

        assertEquals("John", user.getName().getFirstName(), "Expected first name to be John");

        assertEquals("Doe", user.getName().getLastName(), "Expected last name to be Doe");

        assertEquals("example@gmail.com", user.getEmail().getValue(), "Expected email to be example@gmail.com");

        assertEquals("Pepe24123", user.getEncodedPassword().getValue(), "Expected encoded password to be Pepe24123");

        assertTrue(user.getRoles().get().contains("USER"), "Expected roles to contain USER");

        assertEquals(UserStatus.ACTIVE, user.getStatus(), "Expected user status to be ACTIVE");

        assertNotNull(user.getCreatedAt(), "Expected user creation timestamp to be set");

        assertNotNull(resultSuccess.getErrors(), "Expected errors to be present");

        assertTrue(resultSuccess.getErrors().isEmpty(), "Expected errors to be empty");

        // Simulate various errors
        Result<User, DomainError> resultFailure =
                UserId.create(UUID.randomUUID())
                        .flatMap(userId -> FullName.create("", "")
                                .flatMap(fullName -> Email.create("example@gfl.com")
                                        .flatMap(email -> EncodedPassword.create("")
                                                .flatMap(encodedPassword -> Roles.create(List.of("G"))
                                                        .map(roles -> new User(
                                                                userId,
                                                                fullName,
                                                                email,
                                                                encodedPassword,
                                                                roles,
                                                                UserStatus.ACTIVE,
                                                                Instant.now()
                                                        ))))));

        assertTrue(resultFailure.isFailure(), "Expected result to be a failure");

        assertNotNull(resultFailure.getErrors(), "Expected errors to be present");

        assertFalse(resultFailure.getErrors().isEmpty(), "Expected errors to not be empty");

        assertEquals(6, resultFailure.getErrors().size(), "Expected 6 errors to be present");

        List<DomainError> errors = resultFailure.getErrors();

        assertTrue(errors.stream().anyMatch(e -> e.getField().map("firstName"::equals).orElse(false)),
                "Expected error for firstName to be present");

        assertTrue(errors.stream().anyMatch(e -> e.getField().map("lastName"::equals).orElse(false)),
                "Expected error for lastName to be present");

        assertTrue(errors.stream().anyMatch(e -> e.getField().map("email"::equals).orElse(false)),
                "Expected error for email to be present");

        assertTrue(errors.stream().anyMatch(e -> e.getField().map("encodedPassword"::equals).orElse(false)),
                "Expected error for encodedPassword to be present");

        assertTrue(errors.stream().anyMatch(e -> e.getField().map("roles"::equals).orElse(false)),
                "Expected error for roles to be present");
    }
}

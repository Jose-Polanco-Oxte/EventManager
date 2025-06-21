package jpolanco.springbootapp.unit.domain;

import jpolanco.springbootapp.user.domain.model.valueobjects.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email Value Object Test")
public class UserValueObjectsTest {
    @Nested
    @DisplayName("Invalid Email Tests")
    class InvalidEmailTests {

        @Test
        @DisplayName("Should fail to create Email with invalid formats")
        void shouldFailForInvalidEmails() {
            List<String> invalidEmails = Arrays.asList(
                    "",               // Empty email
                    " ",              // Whitespace
                    "null",           // Null string
                    "not-an-email",   // Missing domain
                    "a@b..c",         // Double dot
                    ".a@gmail.com",         // Leading dot
                    "a@gmail.com.",         // Trailing dot
                    "-a@gmail.c",         // Leading hyphen
                    "b$a@gmail.c",          // Invalid character
                    ("a").repeat(65) + "@gmail.com", // Too long user part
                    "a@@gmail.c",       // Double '@'
                    "a@b.c d",         // Contains whitespace
                    "a@b.c@d",         // Multiple '@' signs
                    "gmail.com",    // Missing user part
                    "g_t@example.com" // Invalid domain
            );

            for (String email : invalidEmails) {
                var result = Email.create(email);
                assertTrue(result.isFailure(), "Expected failure for: " + email);
            }
        }
    }

    @Nested
    @DisplayName("Valid Email Tests")
    class ValidEmailTests {

        @Test
        @DisplayName("Should create Email with valid formats")
        void shouldSucceedForValidEmails() {
            List<String> validEmails = List.of(
                    "juan.perez@gmail.com",
                    "maria_lopez89@yahoo.com",
                    "admin@hotmail.com",
                    "user+registro@outlook.com",
                    "nombre.apellido@icloud.com",
                    "cliente123@gmail.com",
                    "correo.test+alias@yahoo.com",
                    "soporte_ventas@hotmail.com",
                    "usuario.outlook@outlook.com",
                    "backup.cloud@icloud.com"
            );

            for (String email : validEmails) {
                var result = Email.create(email);
                assertTrue(result.isSuccess(), "Expected success for: " + email + " but got failure: " + result.getMessage());
                assertEquals(email, result.getValue().getValue());
            }
        }
    }
}
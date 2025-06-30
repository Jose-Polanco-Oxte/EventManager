package jpolanco.springbootapp.unit.domain.user.value_objects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.value_objects.EncodedPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EncodedPassword Value Object Tests")
public class EncodedPasswordTest {
    @Test
    @DisplayName("Should create EncodedPassword with valid format")
    void shouldCreateEncodedPassword() {

        List<String> invalidEncodedPasswords = Arrays.asList("",               // Empty password
                " ",              // Whitespace
                null,           // Null string
                "short"          // Too short (password encoded < 6 characters)
        );
        for (String password : invalidEncodedPasswords) {
            var result = EncodedPassword.create(password);
            assertTrue(result.isFailure(), "Expected failure for: " + password);
        }
    }

    @Test
    @DisplayName("Should fail to create EncodedPassword with invalid formats")
    void shouldFailForEmptyEncodedPassword() {
        List<String> validEncodedPasswords = List.of(
                "encodedPassword123",
                "anotherEncodedPassword456",
                "securePassword789",
                "awd23423@#@!$%&*()",
                "n52nhh34iuiower3453-4*5iuoh34rn34rlkf");

        for (String password : validEncodedPasswords) {
            Result<EncodedPassword> result = EncodedPassword.create(password);
            assertTrue(result.isSuccess(), "Expected success for: " + password + " but got failure: " + result.getMessage());
            // Verify the value is set correctly
            assertEquals(password, result.getValue().getValue());
        }
    }

    @Test
    @DisplayName("Two EncodedPassword objects with same value should be equal")
    void shouldBeEqualForSameEncodedPasswordValues() {
        var passwordOne = EncodedPassword.create("sameEncodedPassword").getValue();
        var passwordTwo = EncodedPassword.create("sameEncodedPassword").getValue();
        assertEquals(passwordOne, passwordTwo, "Expected both EncodedPassword objects to be equal");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        var password = EncodedPassword.create("sameEncodedPassword").getValue();
        assertEquals(password, password, "Expected EncodedPassword to be equal to itself");
    }
}

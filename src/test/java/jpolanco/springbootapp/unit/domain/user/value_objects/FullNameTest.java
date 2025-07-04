package jpolanco.springbootapp.unit.domain.user.value_objects;

import jpolanco.springbootapp.unit.utils.Unicode;
import jpolanco.springbootapp.user.domain.model.valueobjects.FullName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FullName Value Object Tests")
public class FullNameTest {
    @Test
    @DisplayName("Should fail to create FullName with invalid formats")
    void shouldFailForInvalidFullNames() {
        List<String> invalidFullNames = Arrays.asList(
                "",                         // Empty full name
                " ",                        // Whitespace
                null,                       // Null string
                "A",                        // Too short (less than 2 characters)
                "23",                       // Invalid characters (numbers only)
                "A@B",                      // Invalid characters (contains '@')
                "A B C",                    // Valid format but too short (less than 2 characters)
                "pepe.perez",               // Invalid characters (contains dot)
                "Juan123",                  // Invalid characters (contains numbers)
                "\u200B\"Juan Perez\"",     // Invalid characters (contains double quotes)
                "Maria Lopez!",             // Invalid characters (contains exclamation mark)
                "Admin User@",              // Invalid characters (contains '@')
                "\u2020Hola Mundo\u2020",   // Invalid characters (contains dagger symbol)
                "\"\u2005Pedro Gómez\"",    // Invalid characters (contains quotations)
                "Cliente 123",              // Valid format but contains numbers
                "Soporte Ventas#",          // Invalid characters (contains '#')
                "Usuario Outlook$",         // Invalid characters (contains '$')
                "GALAXY S f' ",             // Invalid characters (contains apostrophe)
                "María\u2002\"Torres\u200B",    // quotes and word joiner
                "A very long full name that exceeds the maximum allowed length invoke fifty characters" // Too long
        );
        var invertedList = invalidFullNames.reversed();


        for (String fullName : invalidFullNames) {
            // Both inputs must be invalid for FullName.create
            var result = FullName.create(fullName, fullName);
            assertTrue(result.isFailure(), "Expected failure for: " + Unicode.escapeUnicode(fullName) + " but got success: " + result.getErrorMessage());
        }
        for (int i = 0; i < invalidFullNames.size(); i++) {
            // Test with two different invalid inputs
            var input1 = invalidFullNames.get(i);
            var input2 = invertedList.get(i);
            var result = FullName.create(input1, input2);
            assertTrue(result.isFailure(), "Expected failure for: " + Unicode.escapeUnicode(input1) + " and " + Unicode.escapeUnicode(input2));
        }
    }

    @Test
    @DisplayName("Should create FullName with valid format")
    void shouldCreateFullName() {
        // Assuming FullName is a valid value object similar to Email and EncodedPassword
        List<String> validFullNames = List.of(
                " Juan  Perez ",
                " Maria Lopez ",
                "Admin  User",
                "Soporte Ventas ",
                " Usuario Outlook",
                "Juan\u00A0Pérez",
                "María\u2009Torres",
                "José\u200BAlonso",
                "tony pe rez", "tu yo todos",
                "Meon",
                "Ana\u00A0Lopez",               // No-break space between
                "Luis\u2003Garcia",             // Em space between
                "Colonos Somos todos  XD",
                " Cliente    Debe ser", "José Perez",
                "amor",
                "José\u2009Alonso",             // Thin space between
                "  \u2001Carlos Ruiz\u2001 ",   // Medium mathematical space leading and trailing
                "\u200AEmma\u2004Diaz ",        // Hair space leading and trailing
                "\u2000Lucia\u2006Vega",        // Punctuation space leading and trailing
                " Miguel\u2060Romero"           // Word joiner leading and trailing normal space
        );
        var invertedList = validFullNames.reversed();

        for (String fullName : validFullNames) {
            // Both inputs must be valid for FullName.create
            var result = FullName.create(fullName, fullName);
            assertTrue(result.isSuccess(), "Expected success for: " + Unicode.escapeUnicode(fullName) + " but got failure: " + result.getErrorMessage());
            // Verify the value is set correctly
            var fullNameFormated = fullName.strip().replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
            assertEquals(fullNameFormated, result.getSuccess().getFirstName());
            assertEquals(fullNameFormated, result.getSuccess().getLastName());
            assertEquals(fullNameFormated + " " + fullNameFormated, result.getSuccess().toString());
        }

        for (int i = 0; i < validFullNames.size(); i++) {
            // Test with two different valid inputs
            var input1 = validFullNames.get(i);
            var input2 = invertedList.get(i);
            var result = FullName.create(input1, input2);
            assertTrue(result.isSuccess(), "Expected success for: " + Unicode.escapeUnicode(input1) + " and " + Unicode.escapeUnicode(input2) + " but got failure: " + result.getErrorMessage());
            var input1Formated = input1.strip().replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
            var input2Formated = input2.strip().replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
            // Verify the value is set correctly
            assertEquals(input1Formated, result.getSuccess().getFirstName());
            assertEquals(input2Formated, result.getSuccess().getLastName());
            assertEquals(input1Formated + " " + input2Formated, result.getSuccess().toString());
        }
    }

    @Test
    @DisplayName("Two FullName objects with same values should be equal")
    void shouldBeEqualForSameFullNameValues() {
        var fullNameOne = FullName.create("Same First", "Same Last").getSuccess();
        var fullNameTwo = FullName.create("Same First", "Same Last").getSuccess();
        assertEquals(fullNameOne, fullNameTwo, "Expected both FullName objects to be equal");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        var fullName = FullName.create("Same First", "Same Last");
        assertEquals(fullName, fullName, "Expected FullName to be equal to itself");
    }
}

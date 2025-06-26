package jpolanco.springbootapp.unit.domain;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.unit.utils.Unicode;
import jpolanco.springbootapp.user.domain.model.value_objects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Value Object Test")
public class UserValueObjectsTest {

    /**
     * This class contains tests for the value objects used in the User domain model.
     * It includes tests for:
     * - Email creation with valid and invalid formats
     * - EncodedPassword creation with valid and invalid formats
     * - FullName creation with valid and invalid formats
     * - QRFileName creation with valid and invalid formats
     * - Roles creation with valid and invalid formats, adding and removing roles
     * - UserId creation with valid and invalid UUIDs
     */

    @Nested
    @DisplayName("Email creation tests")
    class InvalidEmailTests {

        @Test
        @DisplayName("Should fail to create Email with invalid formats")
        void shouldFailForInvalidEmails() {
            List<String> invalidEmails = Arrays.asList(
                    "",                 // Empty email
                    " ",                // Whitespace
                    null,               // Null string
                    "not-an-email",     // Missing domain
                    "a@b..c",           // Double dot
                    ".a@gmail.com",     // Leading dot
                    "a@gmail.com.",     // Trailing dot
                    "-a@gmail.c",       // Leading hyphen
                    "b$a@gmail.c",      // Invalid character
                    ("a").repeat(65) + "@gmail.com", // Too long user part
                    "a@@gmail.c",       // Double '@'
                    "a@b.c d",          // Contains whitespace
                    "a@b.c@d",          // Multiple '@' signs
                    "gmail.com",        // Missing user part
                    "g_t@example.com"   // Invalid domain
            );

            for (String email : invalidEmails) {
                var result = Email.create(email);
                assertTrue(result.isFailure(), "Expected failure for: " + email);
            }
        }

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
                    "backup.cloud@icloud.com");

            for (String email : validEmails) {
                var result = Email.create(email);
                assertTrue(result.isSuccess(), "Expected success for: " + email + " but got failure: " + result.getMessage());
                // Verify the value is set correctly
                assertEquals(email, result.getValue().getValue());
            }
        }

        @Test
        @DisplayName("Two email objects with same value should be equal")
        void shouldBeEqualForSameEmailValues() {
            var emailOne = Email.create("same@gmail.com").getValue();
            var emailTwo = Email.create("same@gmail.com").getValue();
            assertEquals(emailOne, emailTwo, "Expected both email objects to be equal");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            var email = Email.create("same@gmail.com").getValue();
            assertEquals(email, email, "Expected email to be equal to itself");
        }
    }

    @Nested
    @DisplayName("Encoded password creation tests")
    class EncodedPasswordTests {

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
                var result = EncodedPassword.create(password);
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

    @Nested
    @DisplayName("Full name creation tests")
    class FullNameTests {

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
                    "A very long full name that exceeds the maximum allowed length of fifty characters" // Too long
            );
            var invertedList = invalidFullNames.reversed();


            for (String fullName : invalidFullNames) {
                // Both inputs must be invalid for FullName.create
                var result = FullName.create(fullName, fullName);
                assertTrue(result.isFailure(), "Expected failure for: " + Unicode.escapeUnicode(fullName));
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
                assertTrue(result.isSuccess(), "Expected success for: " + Unicode.escapeUnicode(fullName) + " but got failure: " + result.getMessage());
                // Verify the value is set correctly
                var fullNameFormated = fullName.strip().replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
                assertEquals(fullNameFormated, result.getValue().getFirstName());
                assertEquals(fullNameFormated, result.getValue().getLastName());
                assertEquals(fullNameFormated + " " + fullNameFormated, result.getValue().toString());
            }

            for (int i = 0; i < validFullNames.size(); i++) {
                // Test with two different valid inputs
                var input1 = validFullNames.get(i);
                var input2 = invertedList.get(i);
                var result = FullName.create(input1, input2);
//                System.err.println("Testing with: " + Unicode.escapeUnicode(input1) + " and " + Unicode.escapeUnicode(input2));
                assertTrue(result.isSuccess(), "Expected success for: " + Unicode.escapeUnicode(input1) + " and " + Unicode.escapeUnicode(input2) + " but got failure: " + result.getMessage());
                var input1Formated = input1.strip().replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
                var input2Formated = input2.strip().replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
                // Verify the value is set correctly
                assertEquals(input1Formated, result.getValue().getFirstName());
                assertEquals(input2Formated, result.getValue().getLastName());
//                System.out.println("FullName created: " + result.getValue().toString());
//                System.out.println("FullName created with Unicode: " + Unicode.escapeUnicode(result.getValue().toString()));
                assertEquals(input1Formated + " " + input2Formated, result.getValue().toString());
            }
        }

        @Test
        @DisplayName("Two FullName objects with same values should be equal")
        void shouldBeEqualForSameFullNameValues() {
            var fullNameOne = FullName.create("Same First", "Same Last").getValue();
            var fullNameTwo = FullName.create("Same First", "Same Last").getValue();
            assertEquals(fullNameOne, fullNameTwo, "Expected both FullName objects to be equal");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            var fullName = FullName.create("Same First", "Same Last").getValue();
            assertEquals(fullName, fullName, "Expected FullName to be equal to itself");
        }
    }

    @Nested
    @DisplayName("QR file name tests")
    class QRFileNameTests {

        @Test
        @DisplayName("Should fail for invalid QR file names")
        void shouldFailForInvalidQRFileNames() {
            List<String> invalidNames = Arrays.asList(
                    "",                     // Empty name
                    " ",                    // Whitespace
                    null,                   // Null string
                    "qr/invalid/name.png"   // Contain extension
            );
            for (String name : invalidNames) {
                var result = QRFileName.create(name);
                assertTrue(result.isFailure(), "Expected failure for: " + name);
            }
        }


        @Test
        @DisplayName("Should create valid QR file names")
        void shouldCreateValidQRFileNames() {
            List<String> validNames = List.of(
                    "qr_file_name",
                    "QRFileName123",
                    "valid_name_456",
                    "Gaw GAWm dtwa_ gw", // Valid, spaces replaced with underscores
                    "another_valid_name",
                    "qr_file_name_with_underscores",
                    "valid-name-789",
                    "qr-file-name-with-dashes",
                    "validNameWithCamelCase",
                    "validNameWithNumbers123"
            );

            for (String name : validNames) {
                var result = QRFileName.create(name);
                assertTrue(result.isSuccess(), "Expected success for: " + name + " but got failure: " + result.getMessage());
                // Verify the value is set correctly
                assertEquals(name.trim().replaceAll(" ", "_"), result.getValue().getValue());
            }
        }

        @Test
        @DisplayName("Two QRFileName objects with same value should be equal")
        void shouldBeEqualForSameQRFileNameValues() {
            var qrFileNameOne = QRFileName.create("same_qr_file_name").getValue();
            var qrFileNameTwo = QRFileName.create("same_qr_file_name").getValue();
            assertEquals(qrFileNameOne, qrFileNameTwo, "Expected both QRFileName objects to be equal");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            var qrFileName = QRFileName.create("same_qr_file_name").getValue();
            assertEquals(qrFileName, qrFileName, "Expected QRFileName to be equal to itself");
        }
    }

    @Nested
    @DisplayName("Roles tests")
    class RolesTests {
        private final List<List<String>> invalidRoles = Arrays.asList(
                List.of(),                                      // Empty list
                List.of(""),                                // List with empty string
                List.of(" "),                               // List with whitespace
                Collections.singletonList(null),                            // List with null
                List.of("invalid_role"),                    // List with invalid role
                List.of("another_invalid_role"),            // Another invalid role
                List.of("invalid_role", "another_invalid_role"),// Multiple invalid roles
                Arrays.asList(null, "valid_role", "", " "),           // Mixed list with null, valid, empty, and whitespace
                null                                            // Null list
        );

        private final List<List<String>> validRoles = Arrays.asList(
                List.of("USER"),                          // Single valid role
                List.of("ORGANIZER"),                     // Single valid role
                List.of("ADMIN"),                         // Single valid role
                List.of("ADMIN", "ORGANIZER"),                // Multiple valid roles
                List.of("USER", "ADMIN", "ORGANIZER"),        // All valid roles
                List.of("USER", "ADMIN"),                     // Two valid roles
                List.of("ORGANIZER", "USER")                  // Another combination of valid roles
        );

        private final List<List<String>> mixedRoles = Arrays.asList(
                List.of("USER", "INVALID_ROLE"), // Valid and invalid roles
                List.of("ORGANIZER", "ANOTHER_INVALID_ROLE"), // Valid and invalid roles
                List.of("ADMIN", "INVALID_ROLE", "ORGANIZER"), // Valid and invalid roles
                List.of("USER", "ADMIN", "INVALID_ROLE"), // Multiple valid and one invalid role
                List.of("INVALID_ROLE", "ANOTHER_INVALID_ROLE", "ORGANIZER"), // Multiple invalid and one valid role
                List.of("USER", "ADMIN", "ORGANIZER", "INVALID_ROLE"), // All valid roles with one invalid
                Arrays.asList("USER", null, "ORGANIZER", "INVALID_ROLE"), // Mixed valid, null, and invalid roles
                List.of("ADMIN", "ORGANIZER", "INVALID_ROLE", "ANOTHER_INVALID_ROLE"), // Multiple valid and multiple invalid roles
                Arrays.asList("ADMIN", "ADMIN", null, "ORGANIZER", "INVALID_ROLE"), // Mixed valid, null, and invalid roles
                Arrays.asList("ADMIN", "USER", null, "ORGANIZER", "INVAawfga", null) // Mixed valid, null, and invalid roles
        );

        @Test
        @DisplayName("Should fail for invalid roles")
        void shouldFailForInvalidRoles() {
            for (var roles : invalidRoles) {
                var result = Roles.create(roles);
                assertTrue(result.isFailure(), "Expected failure for roles: " + roles);
            }
        }

        @Test
        @DisplayName("Should create valid roles")
        void shouldCreateValidRoles() {
            for (var roles : validRoles) {
                var result = Roles.create(roles);
                assertTrue(result.isSuccess(), "Expected success for roles: " + roles + " but got failure: " + result.getMessage());
                // Verify the values are set correctly
                assertEquals(new HashSet<>(roles), new HashSet<>(result.getValue().get()),
                        "Expected roles to match: " + roles + " but got: " + result.getValue().get());
            }
        }

        @Test
        @DisplayName("Should create only valid roles and ignore invalid ones")
        void shouldCreateOnlyValidRoles() {
            for (var roles : mixedRoles) {
                var result = Roles.create(roles);
                assertTrue(result.isSuccess(), "Expected success for roles: " + roles + " but got failure: " + result.getMessage());
                assertTrue(containsOnlyValidRoles(result.getValue().get()),
                        "Expected only valid roles but got: " + result.getValue().get());
//                System.out.println("Roles created: " + result.getValue().getByPages().toString());
            }
        }

        private Set<String> getValidRoles() {
            return Set.of("USER", "ORGANIZER", "ADMIN");
        }
        private boolean containsOnlyValidRoles(List<String> roles) {
            Set<String> validRoles = getValidRoles();
            return validRoles.containsAll(roles);
        }

        @Test
        @DisplayName("Add and remove role")
        void shouldAddAndRemoveRoles() {
            var roles = Roles.create(List.of("USER")).getValue(); // Start with a valid role

            /* TEST: Add new role */
            assertFalse(roles.add("INVALID")); // Attempt to add an invalid role
            assertFalse(roles.add(null)); // Attempt to add null role
            assertTrue(roles.add("ORGANIZER")); // Add a valid role
            assertTrue(roles.add("ADMIN")); // Add another valid role
            assertFalse(roles.add("GUEST")); // Add a role that is not in the valid roles set
            assertFalse(roles.add("ADMIN")); // Attempt to add a duplicate role

            /* TEST: Remove role */
            assertFalse(roles.remove("USER")); // Cannot remove USER
            assertFalse(roles.remove("INVALID")); // Attempt to remove an invalid role
            assertTrue(roles.remove("ORGANIZER")); // Remove another valid role
            assertFalse(roles.remove("ORGANIZER")); // Attempt to remove a role that has already been removed
            assertFalse(roles.remove(null)); // Attempt to remove a null role
        }

        @Test
        @DisplayName("Add and remove with list of roles")
        void shouldAddAndRemoveRolesWithList() {
            var roles = Roles.create(List.of("USER")).getValue(); // Start with a valid role

            /* TEST: Add new roles */

            // Attempt to add invalid roles
            for (var r : invalidRoles) {
                var result = roles.addAll(r);
                assertTrue(result.isEmpty(), "Expected no roles to be added for: " + r);
            }

            // Attempt to add valid roles
            for (var r : validRoles) {
                var result = roles.addAll(r);
                assertTrue(containsOnlyValidRoles(result), "Expected only valid roles to be added but got: " + result);
            }

            // Attempt to add mixed roles
            for (var r : mixedRoles) {
                var result = roles.addAll(r);
                assertTrue(containsOnlyValidRoles(roles.get()), "Expected only valid roles to be added but got: " + result);
            }

            /* TEST: Remove roles */

            // Attempt to remove invalid roles
            for (var r : invalidRoles) {
                var result = roles.removeAll(r);
                assertTrue(result.isEmpty(), "Expected no roles to be removed for: " + r);
            }

            // Attempt to remove valid roles
            for (var r : validRoles.reversed()) {
                var result = roles.removeAll(r);
                assertTrue(containsOnlyValidRoles(result), "Expected only valid roles to be removed but got: " + result);
            }

            // Attempt to remove mixed roles
            for (var r : mixedRoles) {
                var result = roles.removeAll(r);
                assertTrue(containsOnlyValidRoles(roles.get()), "Expected only valid roles to remain but got: " + result);
            }
        }

        @Test
        @DisplayName("Two Roles objects with same values should be equal")
        void shouldBeEqualForSameRolesValues() {
            var rolesOne = Roles.create(List.of("USER", "ADMIN")).getValue();
            var rolesTwo = Roles.create(List.of("USER", "ADMIN")).getValue();
            assertEquals(rolesOne, rolesTwo, "Expected both Roles objects to be equal");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            var roles = Roles.create(List.of("USER", "ADMIN")).getValue();
            assertEquals(roles, roles, "Expected Roles to be equal to itself");
        }
    }

    @Nested
    @DisplayName("User Id tests")
    class UserIdTests {

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
}
package jpolanco.springbootapp.unit.domain.user.value_objects;

import jpolanco.springbootapp.user.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Roles Value Object Tests")
public class RolesTest {
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
            List.of("ORGANIZER", "USER")                  // Another combination invoke valid roles
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
    @DisplayName("Add and remove with list invoke roles")
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

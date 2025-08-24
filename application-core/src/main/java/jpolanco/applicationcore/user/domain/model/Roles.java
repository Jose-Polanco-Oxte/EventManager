package jpolanco.applicationcore.user.domain.model;

import jpolanco.applicationcore.shared.domain.utils.Validators;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.CollectionResult;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.*;

public class Roles {

    private final Set<UserRoles> values;

    private Roles(Set<UserRoles> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty.");
        }
        this.values = Set.copyOf(values);
    }

    public static Result<Roles, DomainError> create(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return Validators.EMPTY_COLLECTION("roles", roles);
        }

        Set<UserRoles> userRolesSet = new HashSet<>();
        List<DomainError> errors = new ArrayList<>();

        for (String role : roles) {
            Optional<UserRoles> maybeUserRole = UserRoles.fromString(role);
            if (maybeUserRole.isPresent()) {
                userRolesSet.add(maybeUserRole.get());
            } else {
                if (role == null || role.isBlank()) {
                    errors.addAll(Validators.EMPTY_VALUE("roles").getErrors()); // Adding all errors from EMPTY_VALUE
                } else {
                    errors.add(DomainError.validation("roles", "Invalid role: " + role)); // Specific error for invalid role
                }
            }
        }

        // Rule: At least one role must be USER
        if (!userRolesSet.contains(UserRoles.USER)) {
            errors.add(DomainError.businessRule("At least one role must be USER."));
        }

        if (!errors.isEmpty()) {
            return Result.failure(errors);
        }

        return Result.success(new Roles(userRolesSet));
    }

    public static Roles loadUnchecked(Set<UserRoles> roles) {
        return new Roles(roles);
    }

    public Result<Roles, DomainError> add(String role) {
        Result<UserRoles, DomainError> validation = validateRole(role);
        if (validation.isFailure()) {
            return Result.failure(validation.getErrors());
        }

        UserRoles roleToAdd = validation.getValue();

        // Rule: Cant add the same role twice
        if (this.values.contains(roleToAdd)) {
            return Result.success(this);
        }

        Set<UserRoles> newValues = new HashSet<>(this.values);
        newValues.add(roleToAdd);
        return Result.success(new Roles(newValues));
    }

    public Result<Roles, DomainError> remove(String role) {
        Result<UserRoles, DomainError> validation = validateRole(role);
        if (validation.isFailure()) {
            return Result.failure(validation.getErrors());
        }

        UserRoles roleToRemove = validation.getValue();

        // Rule: Cant remove the last remaining role or essential roles (USER)
        if (this.values.size() == 1 || (this.values.size() == 2 && this.values.contains(UserRoles.USER) && roleToRemove == UserRoles.USER)) {
            return Result.failure(DomainError.businessRule("Cannot remove the last remaining role or essential roles."));
        }

        if (!this.values.contains(roleToRemove)) {
            return Result.success(this);
        }

        Set<UserRoles> newValues = new HashSet<>(this.values);
        newValues.remove(roleToRemove);
        return Result.success(new Roles(newValues));
    }

    private Result<UserRoles, DomainError> validateRole(String role) {
        Optional<UserRoles> maybeUserRole = UserRoles.fromString(role);

        if (maybeUserRole.isEmpty()) {
            if (role == null || role.isBlank()) {
                return Validators.EMPTY_VALUE("roles");
            } else {
                return Result.failure(DomainError.validation("roles", "Invalid role: " + role));
            }
        }

        return Result.success(maybeUserRole.get());
    }

    public CollectionResult<UserRoles, Roles> addAll(List<String> roles) {
        return iterateMaybeRolesAndValidate(roles, (userRole, added, set) -> {
            if (set.add(userRole)) { // Only add if it wasn't already present
                added.add(userRole); // Track added roles
            }
        });
    }

    public CollectionResult<UserRoles, Roles> removeAll(List<String> roles) {
        return iterateMaybeRolesAndValidate(roles, (userRole, removed, set) -> {
            if (set.remove(userRole)) { // Only remove if it was present
                removed.add(userRole); // Track removed roles
            }
        });
    }

    private CollectionResult<UserRoles, Roles> iterateMaybeRolesAndValidate(List<String> roles, TriConsumer<UserRoles, List<UserRoles>, Set<UserRoles>> function) {
        List<UserRoles> tracker = new ArrayList<>(); // Roles that were added/removed
        List<String> notFound = new ArrayList<>(); // Roles that were not found/invalid

        if (roles == null || roles.isEmpty()) {
            return CollectionResult.failure(Validators.EMPTY_COLLECTION("roles", roles).getErrors());
        }

        Set<UserRoles> newValues = new HashSet<>(this.values);

        // Rule: Skip null, empty or invalid roles
        for (String role : roles) {
            Optional<UserRoles> maybeUserRole = UserRoles.fromString(role);
            if (maybeUserRole.isPresent()) {
                UserRoles userRole = maybeUserRole.get();
                function.accept(userRole, tracker, newValues); // Apply the function to track additions/removals
            } else {
                notFound.add(role == null ? "null" : role); // Track not found/invalid roles
            }
        }

        // Rule: If no valid roles were processed, return failure
        if (tracker.isEmpty()) {
            String notFoundMessage = notFound.isEmpty() ? "No valid roles to process." : "No valid roles to process: " + notFound;
            return CollectionResult.failure(DomainError.validation("roles", notFoundMessage));
        }
        return CollectionResult.success(new Roles(newValues), tracker, null, notFound);
    }

    public List<String> get() {
        return this.values.stream()
                .map(UserRoles::getValue)
                .toList();
    }

    public Set<UserRoles> getAsUserRolesSet() {
        return Set.copyOf(this.values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return Objects.equals(values, roles.values);
    }

    @Override
    public String toString() {
        return "Roles{" +
                "values=" + values +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
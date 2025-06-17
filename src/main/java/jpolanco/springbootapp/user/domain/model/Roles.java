package jpolanco.springbootapp.user.domain.model;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Roles {
    private final Set<String> values;
    private final static Set<String> validRoles = Set.of(
            "USER",
            "ADMIN",
            "ORGANIZER"
    );

    private Roles(Set<String> values) {
        this.values = values;
    }

    public static Result<Roles> create(Set<String> values) {
        if (values == null || values.isEmpty()) {
            return Result.failure(Error.NULL_VALUE);
        }
        var result = isValidRole(values);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(new Roles(values));
    }

    private static Result<Set<String>> isValidRole(Set<String> roles) {
        Set<String> filter = roles
                .stream()
                .filter(validRoles::contains)
                .collect(Collectors.toSet());
        if (filter.isEmpty()) {
            return Result.failure(new Error("INVALID_ROLES", "No valid roles provided"));
        }
        return Result.success(filter);
    }

    public Set<String> getValues() {
        return new HashSet<>(values);
    }

    public void addValue(String role) {
        if (!validRoles.contains(role)) {
            return;
        }
        this.values.add(role);
    }

    public void removeValue(String role) {
        values.remove(role);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Roles roles = (Roles) obj;
        return values.equals(roles.values);
    }
}

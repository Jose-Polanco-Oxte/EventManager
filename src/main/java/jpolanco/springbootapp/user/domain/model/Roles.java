package jpolanco.springbootapp.user.domain.model;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.Role;

import java.util.HashSet;
import java.util.Set;

public class Roles {
    private final Set<Role> values;
    private final static Set<String> validRoles = Set.of(
            "USER",
            "ADMIN",
            "ORGANIZER"
    );

    private Roles(Set<Role> values) {
        this.values = values;
    }

    public static Result<Roles> create(Set<Role> values) {
        if (values == null || values.isEmpty()) {
            return Result.failure(Error.NULL_VALUE);
        }
        for (Role role : values) {
            Result<Role> result = isValidRole(role);
            if (result.isFailure()) {
                return Result.failure(result.getError());
            }
        }
        return Result.success(new Roles(values));
    }

    private static Result<Role> isValidRole(Role role) {
        if (role == null || role.getValue().isBlank()) {
            return Result.failure(Error.NULL_VALUE.field("Role"));
        }
        if (!validRoles.contains(role.getValue())) {
            return Result.failure(new Error("InvalidRole", "Role is not valid"));
        }
        return Result.success(role);
    }

    public Set<Role> getValues() {
        return new HashSet<>(values);
    }

    public Result<Role> addValue(Role role) {
        Result<Role> result = isValidRole(role);
        if (result.isFailure()) {
            return result;
        }
        this.values.add(role);
        return Result.success(role);
    }

    public Result<Role> removeValue(Role role) {
        Result<Role> result = isValidRole(role);
        if (result.isFailure()) {
            return result;
        }
        if (!this.values.remove(role)) {
            throw new IllegalStateException();
        }
        return Result.success(role);
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

package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

import java.util.HashSet;
import java.util.List;
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

    public static Result<Roles> create(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Result.failure(UserDomainError.NULL_VALUE);
        }
        var set = new HashSet<>(values);
        var result = isValidRole(set);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var validRoles = result.getValue();
        return Result.success(new Roles(validRoles));
    }

    private static Result<Set<String>> isValidRole(Set<String> roles) {
        Set<String> filter = roles.stream()
                .filter(r -> r!= null && !r.isBlank() && validRoles.contains(r) )
                .collect(Collectors.toSet());
        if (filter.isEmpty()) {
            return Result.failure(UserDomainError.INVALID_ROLES);
        }
        return Result.success(filter);
    }

    public List<String> get() {
        return values.stream().toList();
    }

    public boolean add(String role) {
        if (role == null || role.isBlank() || !validRoles.contains(role)) {
            return false;
        }
        return this.values.add(role);
    }

    public boolean remove(String role) {
        if (values.size() <= 1) {
            return false;
        }
        return this.values.remove(role);
    }

    public List<String> removeAll(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .filter(this::remove)
                .toList();
    }

    public List<String> addAll(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .filter(this::add)
                .toList();
    }

    public boolean hasRole(String role) {
        return this.values.contains(role);
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

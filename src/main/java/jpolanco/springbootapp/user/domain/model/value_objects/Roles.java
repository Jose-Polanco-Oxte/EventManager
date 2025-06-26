package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.DomainError;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Roles {
    private final Set<UserRoles> values;

    private Roles(Set<UserRoles> values) {
        this.values = values;
    }

    public static Result<Roles> create(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Result.failure(DomainError.NULL_VALUE
                    .withField("List of roles"));
        }
        var result = isValidRole(values);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var validRoles = result.getValue();
        return Result.success(new Roles(validRoles));
    }

    private static Result<Set<UserRoles>> isValidRole(List<String> roles) {
        Set<UserRoles> set = roles.stream()
                .filter(UserRoles::isValidRole)
                .map(UserRoles::fromString)
                .collect(Collectors.toSet());
        if (set.isEmpty()) {
            return Result.failure(DomainError.INVALID_PARAMETER
                    .withDetails("Roles are not valid")
                    .withField("Roles"));
        }
        return Result.success(set);
    }

    public List<String> get() {
        return values.stream()
                .map(UserRoles::getValue)
                .toList();
    }

    public boolean add(String role) {
        if (!UserRoles.isValidRole(role)) {
            return false;
        }
        return this.values.add(UserRoles.fromString(role));
    }

    public boolean remove(String role) {
        if (this.values.size() < 2 || !UserRoles.isValidRole(role) || role.equals(UserRoles.USER.getValue())) {
            return false;
        }
        return this.values.remove(UserRoles.fromString(role));
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
        return this.values.contains(UserRoles.fromString(role));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Roles other)) return false;
        return values.equals(other.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}

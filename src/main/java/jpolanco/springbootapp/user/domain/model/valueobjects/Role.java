package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.domain.ResultBuilder;
import jpolanco.springbootapp.user.domain.errors.RoleError;

public class Role {
    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public static Result<Role> create(String value) {
        return ResultBuilder.from(ensureIsValidRole(value))
                .then( result -> Result.success(new Role(value)))
                .result();
    }

    private static Result<Role> ensureIsValidRole(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure(Error.NULL_VALUE.field("Role"));
        }
        if (value.contains(".")) {
            return Result.failure(RoleError.CONTAIN_DOTS);
        }
        if (value.length() > 20) {
            return Result.failure(RoleError.TOO_LONG);
        }
        if (value.length() < 3) {
            return Result.failure(RoleError.TOOSHORT);
        }
        return Result.success(new Role(value));
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return value.equals(role.value);
    }
}

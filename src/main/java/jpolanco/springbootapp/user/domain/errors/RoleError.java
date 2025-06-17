package jpolanco.springbootapp.user.domain.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class RoleError extends Error {

    public RoleError(String code, String message) {
        super(code, message);
    }

    public static RoleError CONTAIN_DOTS = new RoleError("CONTAIN_DOTS", "Role cannot contain dots");
    public static RoleError TOO_LONG = new RoleError("ROLE_TOO_LONG", "Role cannot be longer than 20 characters");
    public static RoleError TOO_SHORT = new RoleError("ROLE_TOO_SHORT", "Role cannot be shorter than 3 characters");
}

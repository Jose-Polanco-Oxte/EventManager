package jpolanco.springbootapp.user.infrastructure.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class UserInfrastructureError extends Error {
    public UserInfrastructureError(int code, String message) {
        super(code, message);
    }

    public static final UserInfrastructureError INVALID_AUTH_HEADER =
            new UserInfrastructureError(400, "Invalid Authorization header format. Expected 'Bearer'");

    public static final UserInfrastructureError INVALID_CLAIMS_REFRESH_TOKEN =
            new UserInfrastructureError(401, "Invalid claims in refresh token");

    public static final UserInfrastructureError INVALID_CLAIMS_ACCESS_TOKEN =
            new UserInfrastructureError(401, "Invalid claims in access token");

    public static final UserInfrastructureError INVALID_REFRESH_TOKEN =
            new UserInfrastructureError(401, "Invalid refresh token");

    public static final UserInfrastructureError INVALID_ACCESS_TOKEN =
            new UserInfrastructureError(401, "Invalid access token");

    // 404 - Not Found: Usuario no encontrado
    public static final UserInfrastructureError USER_NOT_FOUND =
            new UserInfrastructureError(404, "User not found");
}
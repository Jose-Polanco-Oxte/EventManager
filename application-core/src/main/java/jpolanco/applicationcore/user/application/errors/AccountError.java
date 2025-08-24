package jpolanco.applicationcore.user.application.errors;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.HttpStatusCode;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import org.springframework.http.HttpStatus;

public class AccountError {
    private final static HttpStatusCode UNAUTHORIZED = new HttpStatusCode(HttpStatus.UNAUTHORIZED);
    private final static HttpStatusCode CONFLICT = new HttpStatusCode(HttpStatus.CONFLICT);
    private final static HttpStatusCode BAD_REQUEST = new HttpStatusCode(HttpStatus.BAD_REQUEST);

    public static ServiceError EmailAlreadyInUse(String email) {
        return new ServiceError(
                "Email already in use",
                "The email '" + email + "' is already registered.",
                CONFLICT,
                "email",
                null
        );
    }

    public static ServiceError SessionLimitExceeded(int limit) {
        String message = String.format("Session limit exceeded. Maximum allowed is %d.", limit);
        return new ServiceError(message, null, BAD_REQUEST, null, null);
    }

    public static ServiceError invalidJwtToken() {
        return new ServiceError(
                "Invalid JWT token",
                "The provided JWT token is invalid or expired.",
                UNAUTHORIZED,
                null,
                null
        );
    }
}

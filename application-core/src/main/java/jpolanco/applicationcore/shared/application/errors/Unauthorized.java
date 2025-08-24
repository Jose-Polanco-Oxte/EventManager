package jpolanco.applicationcore.shared.application.errors;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.HttpStatusCode;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import org.springframework.http.HttpStatus;

/**
 * Unauthorized error utility class for creating standardized unauthorized error responses.
 */
public class Unauthorized {
    private final static HttpStatusCode UNAUTHORIZED = new HttpStatusCode(HttpStatus.UNAUTHORIZED);

    public static ServiceError unauthorizedError(String message) {
        return new ServiceError(message, null, UNAUTHORIZED, null, null);
    }
}

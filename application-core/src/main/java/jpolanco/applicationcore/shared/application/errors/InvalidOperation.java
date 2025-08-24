package jpolanco.applicationcore.shared.application.errors;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.HttpStatusCode;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import org.springframework.http.HttpStatus;

/**
 * InvalidOperation error utility class for creating standardized forbidden action error responses.
 */
public class InvalidOperation {
    private final static HttpStatusCode FORBIDDEN = new HttpStatusCode(HttpStatus.FORBIDDEN);

    public static ServiceError invalidActionError(String message) {
        return new ServiceError(message, null, FORBIDDEN, null, null);
    }

    public static ServiceError invalidActionError(String message, String actionRequired) {
        return new ServiceError(message, null, FORBIDDEN, null, actionRequired);
    }
}

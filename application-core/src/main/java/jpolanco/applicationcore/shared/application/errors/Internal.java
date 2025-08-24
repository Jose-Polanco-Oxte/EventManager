package jpolanco.applicationcore.shared.application.errors;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.HttpStatusCode;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import org.springframework.http.HttpStatus;

/**
 * Internal error utility class for creating standardized internal server error responses.
 */
public class Internal {
    private final static HttpStatusCode INTERNAL_ERROR = new HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

    public static ServiceError internalError(String details) {
        return new ServiceError(null, details, INTERNAL_ERROR, null, null);
    }
}

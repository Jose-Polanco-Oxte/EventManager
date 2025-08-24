package jpolanco.applicationcore.shared.application.errors;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.HttpStatusCode;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import org.springframework.http.HttpStatus;

/**
 * NotFound error utility class for creating standardized not found error responses.
 */
public class NotFound {
    private final static HttpStatusCode NOT_FOUND = new HttpStatusCode(HttpStatus.NOT_FOUND);

    public static ServiceError entityNotFound(String entityType, String id) {
        String details = String.format("%s with ID '%s' not found", entityType, id);
        return new ServiceError(entityType + " not found", details, NOT_FOUND, null, null);
    }

    public static ServiceError resourceNotFound(String resource, String field, String value) {
        String details = String.format("%s with %S '%s' not found", resource, field, value);
        return new ServiceError(resource + " not found", details, NOT_FOUND, null, null);
    }
}

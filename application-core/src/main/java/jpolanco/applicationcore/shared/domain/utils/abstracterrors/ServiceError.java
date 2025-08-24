package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import java.util.Objects;
import java.util.Optional;

/**
 * Record representing a service error with details such as message, type, field, and action required.
 * <p>
 * Implements the {@link IError} interface to provide error details.
 * </p>
 */
public record ServiceError(
        String message,
        String details,
        HttpStatusCode type,
        String field,
        String actionRequired
) implements IError {

    @Override
    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }

    @Override
    public HttpStatusCode getType() {
        return type;
    }

    public Optional<String> getField() {
        return Optional.ofNullable(field);
    }

    public Optional<String> getActionRequired() {
        return Optional.ofNullable(actionRequired);
    }

    public ServiceError withActionRequired(String actionRequired) {
        return new ServiceError(
                getMessage().orElse(null),
                getDetails().orElse(null),
                getType(),
                getField().orElse(null),
                actionRequired
        );
    }

    public ServiceError withMessage(String message) {
        return new ServiceError(
                message,
                getDetails().orElse(null),
                getType(),
                getField().orElse(null),
                getActionRequired().orElse(null)
        );
    }

    public ServiceError withDetails(String details) {
        return new ServiceError(
                getMessage().orElse(null),
                details,
                getType(),
                getField().orElse(null),
                getActionRequired().orElse(null)
        );
    }

    public ServiceError withField(String field) {
        return new ServiceError(
                getMessage().orElse(null),
                getDetails().orElse(null),
                getType(),
                field,
                getActionRequired().orElse(null)
        );
    }

    public ServiceError withType(HttpStatusCode type) {
        return new ServiceError(
                getMessage().orElse(null),
                getDetails().orElse(null),
                type,
                getField().orElse(null),
                getActionRequired().orElse(null)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceError(
                String message1, String details1, HttpStatusCode type1, String field1, String required
        ))) return false;
        return Objects.equals(message, message1) &&
                Objects.equals(details, details1) &&
                Objects.equals(type, type1) &&
                Objects.equals(field, field1) &&
                Objects.equals(actionRequired, required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, details, type, field, actionRequired);
    }
}

package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import java.util.Objects;
import java.util.Optional;

/**
 * Class representing a domain error with details such as message, type, field, and additional details.
 * <p>
 * {@link DomainErrType} enum defines the type of domain error.
 * {@link IError} interface defines the contract for error classes.
 * </p>
 */
public class DomainError implements IError {

    private final String message;

    private final String details;

    private final DomainErrType type;

    private final String field;

    public DomainError(String message, String details, DomainErrType type, String field) {
        this.message = message;
        this.details = details;
        this.type = type;
        this.field = field;
    }

    // This is a factory method to create a validation error
    public static DomainError validation(String field, String message) {
        return new DomainError(message, null, DomainErrType.VALIDATION_ERROR, field);
    }

    // This is a factory method to create a business rule violation error
    public static DomainError businessRule(String message) {
        return new DomainError(message, null, DomainErrType.BUSINESS_RULE_VIOLATION, null);
    }

    public DomainError withField(String field) {
        return new DomainError(
                getMessage().orElse(null),
                getDetails().orElse(null),
                getType(),
                field);
    }

    public DomainError withMessage(String message) {
        return new DomainError(
                message,
                getDetails().orElse(null),
                getType(),
                getField().orElse(null));
    }


    public DomainError withDetails(String details) {
        return new DomainError(
                getMessage().orElse(null),
                details,
                getType(),
                getField().orElse(null));
    }

    @Override
    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    @Override
    public DomainErrType getType() {
        return type;
    }

    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }

    public Optional<String> getField() {
        return Optional.ofNullable(field);
    }

    @Override
    public String toString() {
        return "ServiceError{" +
                "message='" + getMessage().orElse("N/A") + '\'' +
                ", details='" + getDetails().orElse("N/A") + '\'' +
                ", type='" + getType().get().orElse("N/A") + '\'' +
                ", field='" + getField().orElse("N/A") + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainError that)) return false;
        return message.equals(that.message) &&
                type.equals(that.type) &&
                field.equals(that.field) &&
                (Objects.equals(details, that.details));
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, details, type, field);
    }
}

package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * Record representing an HTTP status code as an error type.
 * <p>Implements the {@link ErrorTypeV} interface to provide the status name as an optional string.
 */
public record HttpStatusCode(HttpStatus status) implements ErrorTypeV {

    @Override
    public Optional<String> get() {
        return Optional.of(status.name());
    }
}

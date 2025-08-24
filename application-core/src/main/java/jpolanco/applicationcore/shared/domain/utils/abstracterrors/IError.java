package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import java.util.Optional;

/**
 * Interface representing an error with an optional message and a type.
 * <p>{@link #getMessage()} returns an Optional containing the error message if present, or an empty Optional if not.
 * <p>{@link #getType()} returns the type of the error as an {@link ErrorTypeV}.
 */
public interface IError {
    Optional<String> getMessage();

    ErrorTypeV getType();
}
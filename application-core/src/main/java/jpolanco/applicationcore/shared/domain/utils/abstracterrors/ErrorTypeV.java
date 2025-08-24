package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import java.util.Optional;

/**
 * Interface representing a type of error that can provide an optional string value.
 * <p>{@link #get()} returns an Optional containing the error message if present, or an empty Optional if not.
 */
public interface ErrorTypeV {
    Optional<String> get();
}

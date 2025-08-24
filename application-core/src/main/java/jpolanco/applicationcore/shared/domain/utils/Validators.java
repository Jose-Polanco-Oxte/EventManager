package jpolanco.applicationcore.shared.domain.utils;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;

/**
 * A utility class providing static methods for generating standardized validation error results.
 * Each method returns a Result object encapsulating a DomainError with a specific validation message.
 * The class includes an enumeration for common special characters to enhance error message clarity.
 * Example usage:
 * <pre>
 * Result<String, DomainError> result = Validators.EMPTY_VALUE("username");
 * if (result.isFailure()) {
 *     DomainError error = result.getError();
 *     // Handle the error
 * }
 * </pre>
 */
public class Validators {
    public static <T> Result<T, DomainError> EMPTY_VALUE(String field) {
        return Result.failure(DomainError.validation(field, "Value cannot be null or empty"));
    }

    public static <T> Result<T, DomainError> EMPTY_COLLECTION(String field, Iterable<?> value) {
        return Result.failure(DomainError.validation(field, "Collection cannot be null or empty").withDetails("Value: '" + value + "'"));
    }

    public static <T> Result<T, DomainError> TOO_LONG(String field, String value, int max) {
        return Result.failure(DomainError.validation(field, "Value cannot be longer than " + max + " characters").withDetails("Value: '" + value + "'"));
    }

    public static <T> Result<T, DomainError> TOO_SHORT(String field, String value, int min) {
        return Result.failure(DomainError.validation(field, "Value cannot be shorter than " + min + " characters").withDetails("Value: '" + value + "'"));
    }

    public static <T> Result<T, DomainError> INVALID_FORMAT(String field, String value, String message) {
        return Result.failure(DomainError.validation(field, message).withDetails("Value: '" + value + "'"));
    }

    public static <T> Result<T, DomainError> buildPrefixError(String field, String value, String prefix, String message) {
        CharName charName = CharName.fromString(prefix); // Check if the prefix is a known special character
        return Result.failure(DomainError.validation(field, message + (charName != null ? " " + charName.getName() + " '" + charName.getCharacter() + "'" // Use the character name if it's a known special character
                : " '" + prefix + "'") // Otherwise, use the prefix as is
        ).withDetails("Value: '" + value + "'"));
    }

    public static <T> Result<T, DomainError> MUST_NOT_CONTAIN(String field, String value, String substring) {
        return buildPrefixError(field, value, substring, "Value must not contain");
    }

    public static <T> Result<T, DomainError> MUST_NOT_START_WITH(String field, String value, String prefix) {
        return buildPrefixError(field, value, prefix, "Value must not start with");
    }

    private enum CharName {
        DOT(".", "dot"), UNDERSCORE("_", "underscore"), DASH("-", "dash"), AT("@", "at"), SLASH("/", "slash"), BACKSLASH("\\", "backslash"), COLON(":", "colon"), SEMICOLON(";", "semicolon"), COMMA(",", "comma"), SPACE(" ", "space"), EQUALS("=", "equals"), PLUS("+", "plus"), QUESTION_MARK("?", "question mark"), AMPERSAND("&", "ampersand");

        private final String character;
        private final String name;

        CharName(String character, String name) {
            this.character = character;
            this.name = name;
        }

        public static CharName fromString(String value) {
            for (CharName charName : values()) {
                if (charName.getCharacter().equals(value)) {
                    return charName;
                }
            }
            return null;
        }

        public String getCharacter() {
            return character;
        }

        public String getName() {
            return name;
        }
    }
}

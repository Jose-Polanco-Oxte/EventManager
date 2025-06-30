package jpolanco.springbootapp.shared.utils;


import jpolanco.springbootapp.shared.domain.utils.DomainError;
import java.util.Optional;
import java.util.regex.Pattern;

public class Validators {
    private enum CharName {
        DOT(".", "dot"),
        UNDERSCORE("_", "underscore"),
        DASH("-", "dash"),
        AT("@", "at"),
        SLASH("/", "slash"),
        BACKSLASH("\\", "backslash"),
        COLON(":", "colon"),
        SEMICOLON(";", "semicolon"),
        COMMA(",", "comma"),
        SPACE(" ", "space"),
        EQUALS("=", "equals"),
        PLUS("+", "plus"),
        QUESTION_MARK("?", "question mark"),
        AMPERSAND("&", "ampersand");

        private final String character;
        private final String name;

        CharName(String character, String name) {
            this.character = character;
            this.name = name;
        }

        public String getCharacter() {
            return character;
        }

        public String getName() {
            return name;
        }

        public static CharName fromString(String value) {
            for (CharName charName : values()) {
                if (charName.getCharacter().equals(value)) {
                    return charName;
                }
            }
            return null;
        }

        public static String getRegex() {
            StringBuilder regex = new StringBuilder();
            for (CharName charName : values()) {
                regex.append(charName.getCharacter());
            }
            return "[" + regex + "]";
        }
    }

    public static Optional<DomainError> notBlank(String field, String value) {
        return (value == null || value.isBlank())
                ? Optional.of(DomainError.NULL_VALUE
                .withField(field))
                : Optional.empty();
    }

    public static Optional<DomainError> notEmptyList(String field, Iterable<?> value) {
        return (value == null || !value.iterator().hasNext())
                ? Optional.of(DomainError.NULL_VALUE
                .withField(field))
                : Optional.empty();
    }

    public static Optional<DomainError> maxLength(String field, String value, int max) {
        return (value != null && value.length() > max)
                ? Optional.of(DomainError.TOO_LONG
                .withField(field)
                .withDetails("must have at most " + max + " characters"))
                : Optional.empty();
    }

    public static Optional<DomainError> minLength(String field, String value, int min) {
        return (value != null && value.length() < min)
                ? Optional.of(DomainError.TOO_SHORT
                .withField(field)
                .withDetails("must have at least " + min + " characters"))
                : Optional.empty();
    }

    public static Optional<DomainError> regex(String field, String value, String regex, String message) {
        return (value != null && !regex.matches(regex))
                ? Optional.of(DomainError.INVALID_FORMAT.withField(field).withDetails(message))
                : Optional.empty();
    }

    public static Optional<DomainError> mustNotContain(String field, String value, String substring) {
        CharName charName = CharName.fromString(substring);
        return (value != null && value.contains(substring))
                ? Optional.of(DomainError.INVALID_FORMAT
                .withField(field)
                .withDetails("must not contain " + (charName != null ? charName.name + " '" + charName.character + "'" : "'" + substring + "'")))
                : Optional.empty();
    }

    public static Optional<DomainError> mustNotStartWith(String field, String value, String prefix) {
        CharName charName = CharName.fromString(prefix);
        return (value != null && value.startsWith(prefix))
                ? Optional.of(DomainError.INVALID_FORMAT
                .withField(field)
                .withDetails("must not start with" + (charName != null ? " " + charName.name + " '" + charName.character + "'" : " '" + prefix + "'")))
                : Optional.empty();
    }
}

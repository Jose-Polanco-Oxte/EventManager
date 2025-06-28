package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.Set;
import java.util.regex.Pattern;

public class Email {
    private final String value;

    private static final Pattern SPECIAL_CHARACTERS_PATTERN = Pattern.compile("[^a-zA-Z0-9.%+-]");

    private Email(String email) {
        this.value = email;
    }

    private static final Set<String> validDomains = Set.of(
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "icloud.com"
    );

    private static Result<Void> valid(String email) {
        if (email == null || email.isBlank()) {
            return Result.failure(DomainError.NULL_VALUE
                    .withField("Email"));
        }
        var split = email.split("@");
        if (split.length != 2) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("Must contain exactly one '@' character")
                    .withField("email")
            );
        }
        var userName = split[0];
        var domain = split[1];
        if (email.startsWith(".")) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("Cannot start with a dot")
                    .withField("email"));
        }
        if (SPECIAL_CHARACTERS_PATTERN.matcher(userName).matches() || SPECIAL_CHARACTERS_PATTERN.matcher(domain).matches()) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("Cannot contain special characters")
                    .withField("email"));
        }
        if (domain.contains(" ") || userName.contains(" ")) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("Cannot contain whitespace")
                    .withField("email"));
        }
        if (userName.length() > 64) {
            return Result.failure(DomainError.TOO_LONG
                    .withDetails("Email user name must be between 1 and 64 characters long")
                    .withField("email"));
        }
        if (!validDomains.contains(domain)) {
            return Result.failure(DomainError.of(400, "Invalid domain")
                    .withDetails("Email domain is not valid: " + domain)
                    .withField("email"));
        }
        return Result.success();
    }

    public static Result<Email> create(String value) {
        var valid = valid(value);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        return Result.success(new Email(value));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

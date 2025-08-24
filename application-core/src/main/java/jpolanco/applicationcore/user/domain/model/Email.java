package jpolanco.applicationcore.user.domain.model;

import jpolanco.applicationcore.shared.domain.utils.Validators;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;

import java.util.Set;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern SPECIAL_CHARACTERS_PATTERN = Pattern.compile("[^a-zA-Z0-9.%+-]");

    // TODO: Maybe changes to a more flexible domain validation in the future
    private static final Set<String> validDomains = Set.of(
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "icloud.com"
    );

    private final String value;

    private Email(String email) {
        this.value = email;
    }

    public static Result<Email, DomainError> create(String value) {
        Result<Void, DomainError> result = valid(value);
        if (result.isFailure()) {
            return Result.failure(result.getErrors());
        }
        return Result.success(new Email(value));
    }

    protected static Email loadUnchecked(String email) {
        return new Email(email);
    }

    private static Result<Void, DomainError> valid(String value) {
        if (value == null || value.isBlank()) {
            return Validators.EMPTY_VALUE("email");
        }

        String[] split = value.split("@"); // Simple split to check basic structure
        if (split.length != 2) {
            return Validators.INVALID_FORMAT("email", value, "Must contain exactly one '@' character");
        }
        String userName = split[0];
        String domain = split[1];

        if (userName.startsWith(".")) {
            return Validators.MUST_NOT_START_WITH("user_name", value, ".");
        }

        if (SPECIAL_CHARACTERS_PATTERN.matcher(userName).matches() || SPECIAL_CHARACTERS_PATTERN.matcher(domain).matches()) {
            return Validators.INVALID_FORMAT("email", value, "Contains invalid characters");
        }

        if (domain.isBlank() || userName.isBlank()) {
            return Validators.EMPTY_VALUE("email");
        }

        if (userName.length() > 64) {
            return Validators.TOO_LONG("user_name", value, 64);
        }

        if (!validDomains.contains(domain)) {
            return Validators.INVALID_FORMAT("domain_email", value, "Is not a valid email domain");
        }
        return Result.success();
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

    @Override
    public String toString() {
        return value;
    }
}

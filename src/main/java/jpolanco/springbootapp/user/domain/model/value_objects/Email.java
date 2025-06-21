package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

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
            return Result.failure(UserDomainError.NULL_VALUE.field("Email")); // Email must not be null or blank
        }
        var split = email.split("@");
        if (split.length != 2) {
            return Result.failure(UserDomainError.MORE_THAN_ONE_SIGN_IN_EMAIL);
        }
        var userName = split[0];
        var domain = split[1];
        if (email.startsWith(".")) {
            return Result.failure(UserDomainError.EMAIL_STARTS_WITH_DOT);
        }
        if (SPECIAL_CHARACTERS_PATTERN.matcher(userName).matches() || SPECIAL_CHARACTERS_PATTERN.matcher(domain).matches()) {
            return Result.failure(UserDomainError.INVALID_EMAIL_FORMAT);
        }
        if (domain.contains(" ") || userName.contains(" ")) {
            return Result.failure(UserDomainError.EMAIL_CONTAINS_WHITESPACE);
        }
        if (userName.isEmpty() || userName.length() > 64) {
            return Result.failure(UserDomainError.EMAIL_USER_NAME_LENGTH);
        }
        if (!validDomains.contains(domain)) {
            return Result.failure(UserDomainError.INVALID_EMAIL_DOMAIN);
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
}

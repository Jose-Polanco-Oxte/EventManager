package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Validators;

import java.util.Optional;
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
        Optional<DomainError> error = Validators.notBlank("email", email);
        if (error.isPresent()) return Result.failure(error.get());

        var split = email.split("@");
        if (split.length != 2) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("must contain exactly one '@' character")
                    .withField("email")
            );
        }
        var userName = split[0];
        var domain = split[1];

        error = Validators.mustNotStartWith("email userId name", userName, ".");
        if (error.isPresent()) return Result.failure(error.get());

        if (SPECIAL_CHARACTERS_PATTERN.matcher(userName).matches() || SPECIAL_CHARACTERS_PATTERN.matcher(domain).matches()) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("email contains invalid characters")
                    .withField("email"));
        }

        error = Validators.mustNotContain("email", domain, " ");
        if (error.isPresent()) return Result.failure(error.get());

        error = Validators.mustNotContain("email", userName, " ");
        if (error.isPresent()) return Result.failure(error.get());

        error = Validators.maxLength("email userId name", userName, 64);
        if (error.isPresent()) return Result.failure(error.get());

        if (!validDomains.contains(domain)) {
            return Result.failure(DomainError.of(400, "invalid domain")
                    .withDetails("email domain is not valid: " + domain)
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

    protected static Email loadUnchecked(String email) {
        return new Email(email);
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

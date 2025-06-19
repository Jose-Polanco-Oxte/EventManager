package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

public class Email {
    private final String value;

    private Email(String email) {
        this.value = email;
    }

    public static Result<Email> create(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure(UserDomainError.NULL_VALUE.field("Email"));
        }
        String EMAIL_REGEX = "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}";
        if (!value.matches(EMAIL_REGEX)) {
            return Result.failure(UserDomainError.INVALID_EMAIL_FORMAT);
        }
        return Result.success(new Email(value));
    }

    public String getValue() {
        return value;
    }
}

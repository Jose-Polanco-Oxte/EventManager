package jpolanco.springbootapp.user.domain.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class UserDomainError extends Error {
    public UserDomainError(int code, String message) {
        super(code, message);
    }

    public static final UserDomainError NAME_TOO_SHORT =
            new UserDomainError(400, "Name must be at least 2 characters long");

    public static final UserDomainError NAME_TOO_LONG =
            new UserDomainError(400, "Name must be at most 50 characters long");

    public static final UserDomainError NAME_INVALID_CHARACTERS =
            new UserDomainError(422, "Name must contain only letters");

    public static final UserDomainError INVALID_ROLE =
            new UserDomainError(422, "Role is not valid");

    public static final UserDomainError INVALID_EMAIL_FORMAT =
            new UserDomainError(422, "Email format is invalid");

    public static final UserDomainError INVALID_STATUS =
            new UserDomainError(422, "User status is invalid");

    public static final UserDomainError INVALID_ID =
            new UserDomainError(400, "User ID is invalid");

    public static final UserDomainError MORE_THAN_ONE_SIGN_IN_EMAIL =
            new UserDomainError(422, "Email must contain only one '@' sign");

    public static final UserDomainError EMAIL_STARTS_WITH_DOT =
            new UserDomainError(422, "Email must not start with a dot");

    public static final UserDomainError INVALID_EMAIL_DOMAIN =
            new UserDomainError(422, "Email domain is not valid");

    public static final UserDomainError EMAIL_CONTAINS_WHITESPACE =
            new UserDomainError(422, "Email must not contain whitespace");

    public static final UserDomainError EMAIL_USER_NAME_LENGTH =
            new UserDomainError(422, "Email user name must be between 1 and 64 characters long");
}


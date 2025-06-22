package jpolanco.springbootapp.user.application.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class UserAppError extends Error {

    public UserAppError(int code, String message) {
        super(code, message);
    }

    public static final UserAppError USER_NOT_FOUND =
            new UserAppError(404, "User not found");

    public static final UserAppError EMAIL_IS_ALREADY_IN_USE =
            new UserAppError(409, "Email is already in use");

    public static final UserAppError USER_NOT_ACTIVE =
            new UserAppError(403, "User not active");

    public static final UserAppError USER_NOT_AUTHORIZED =
            new UserAppError(401, "Email or password is incorrect");

    public static final UserAppError INVALID_TOKEN =
            new UserAppError(401, "Token is invalid");

    public static final UserAppError INVALID_HEADER =
            new UserAppError(400, "Format of authorization header is invalid");

    public static final UserAppError INVALID_ID_FORMAT =
            new UserAppError(400, "Format of UUID is invalid");

    public static final UserAppError PASSWORDS_ARE_SAME =
            new UserAppError(422, "On change password is the same as old password");

    public static final UserAppError OLD_PASSWORD_IS_INCORRECT =
            new UserAppError(403, "Old password is incorrect");

    public static final UserAppError SESSION_LIMIT_REACHED =
            new UserAppError(429, "Session limit reached for user");

    public static final UserAppError USER_SUSPENDED =
            new UserAppError(403, "User is suspended");

    public static final UserAppError USER_ALREADY_ACTIVE =
            new UserAppError(409, "User is already active");
}

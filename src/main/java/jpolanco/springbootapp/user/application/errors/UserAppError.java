package jpolanco.springbootapp.user.application.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class UserAppError extends Error {

    public UserAppError(String code, String message) {
        super(code, message);
    }

    public final static UserAppError USER_NOT_FOUND = new UserAppError("USER_NOT_FOUND", "User not found");
    public final static UserAppError EMAIL_IS_ALREADY_IN_USE = new UserAppError("EMAIL_IS_ALREADY_IN_USE", "Email is already in use");
    public final static UserAppError USER_NOT_ACTIVE = new UserAppError("USER_NOT_ACTIVE", "User not active");
    public final static UserAppError USER_NOT_AUTHORIZED = new UserAppError("USER_NOT_AUTHORIZED", "Email or password is incorrect");
    public final static UserAppError INVALID_TOKEN = new UserAppError("INVALID_TOKEN", "Token is invalid");
    public final static UserAppError INVALID_HEADER = new UserAppError("INVALID_HEADER", "Format of authorization header is invalid");
    public final static UserAppError INVALID_ID_FORMAT = new UserAppError("INVALID_ID_FORMAT", "Format of UUID is invalid");
    public final static UserAppError PASSWORDS_ARE_SAME = new UserAppError("PASSWORDS_ARE_SAME", "On change password is the same as old password");
    public final static UserAppError OLD_PASSWORD_IS_INCORRECT = new UserAppError("OLD_PASSWORD_IS_INCORRECT", "Old password is incorrect");
    public final static UserAppError SESSION_LIMIT_REACHED = new UserAppError("SESSION_LIMIT_REACHED", "Session limit reached for user");
    public final static UserAppError USER_SUSPENDED = new UserAppError("USER_SUSPENDED", "User is suspended");
}
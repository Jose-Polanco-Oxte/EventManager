package jpolanco.applicationcore.user.infrastructure.exceptions;

public class UserDataConflictException extends RuntimeException {
    public UserDataConflictException(String message) {
        super(message);
    }
}

package jpolanco.applicationcore.config.exceptions;

import jpolanco.applicationcore.shared.utils.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(String message) {
        super(message);
    }
}

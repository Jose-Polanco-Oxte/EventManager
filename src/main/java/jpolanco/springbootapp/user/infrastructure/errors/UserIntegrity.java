package jpolanco.springbootapp.user.infrastructure.errors;

import jpolanco.springbootapp.shared.infrastructure.errors.ProviderException;

public class UserIntegrity extends ProviderException {
    public UserIntegrity(String message, int code, Throwable cause) {
        super(message, code, cause);
    }

    public UserIntegrity(String message, int code) {
        super(message, code);
    }
}

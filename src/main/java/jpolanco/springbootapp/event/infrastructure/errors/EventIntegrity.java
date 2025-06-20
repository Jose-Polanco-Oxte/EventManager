package jpolanco.springbootapp.event.infrastructure.errors;

import jpolanco.springbootapp.shared.infrastructure.errors.ProviderException;

public class EventIntegrity extends ProviderException {
    public EventIntegrity(String message, int code, Throwable cause) {
        super(message, code, cause);
    }

    public EventIntegrity(String message, int code) {
        super(message, code);
    }
}

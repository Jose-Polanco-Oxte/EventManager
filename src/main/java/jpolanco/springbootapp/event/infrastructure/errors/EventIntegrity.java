package jpolanco.springbootapp.event.infrastructure.errors;

public class EventIntegrity extends RuntimeException {
    public EventIntegrity(String message) {
        super(message);
    }
}

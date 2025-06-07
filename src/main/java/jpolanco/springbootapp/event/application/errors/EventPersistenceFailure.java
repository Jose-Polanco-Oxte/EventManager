package jpolanco.springbootapp.event.application.errors;

public class EventPersistenceFailure extends RuntimeException {
    public EventPersistenceFailure(String message) {
        super(message);
    }
}

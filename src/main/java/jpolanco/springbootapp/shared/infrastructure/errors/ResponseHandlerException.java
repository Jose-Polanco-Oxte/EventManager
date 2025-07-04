package jpolanco.springbootapp.shared.infrastructure.errors;

public class ResponseHandlerException extends RuntimeException {
    public ResponseHandlerException(String message) {
        super(message);
    }
}

package jpolanco.springbootapp.shared.infrastructure.controllers;

public class ResponseHandlerException extends RuntimeException {
    public ResponseHandlerException(String message) {
        super(message);
    }
}

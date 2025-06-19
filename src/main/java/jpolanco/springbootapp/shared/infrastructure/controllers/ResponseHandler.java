package jpolanco.springbootapp.shared.infrastructure.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseHandler {

    public static ResponseEntity<Object> ok(Object data) {
        return buildResponse("Request was successful", HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> serverError(String message) {
        return buildResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public static ResponseEntity<Object> ok(String message) {
        return buildResponse(message, HttpStatus.OK, null);
    }

    public static ResponseEntity<Object> created(Object data) {
        return buildResponse("Resource created successfully", HttpStatus.CREATED, data);
    }

    public static ResponseEntity<Object> created(String message) {
        return buildResponse(message, HttpStatus.CREATED, null);
    }

    public static ResponseEntity<Object> noContent() {
        return buildResponse("No content available", HttpStatus.NO_CONTENT, null);
    }

    public static ResponseEntity<Object> notFound() {
        return buildResponse("Resource not found", HttpStatus.NOT_FOUND, null);
    }

    public static ResponseEntity<Object> conflict(String message) {
        return buildResponse(message, HttpStatus.CONFLICT, null);
    }

    public static ResponseEntity<Object> badRequest(String message) {
        return buildResponse(message, HttpStatus.BAD_REQUEST, null);
    }

    public static ResponseEntity<Object> methodNotAllowed(String message) {
        return buildResponse(message, HttpStatus.METHOD_NOT_ALLOWED, null);
    }

    public static ResponseEntity<Object> unauthorized(String message) {
        return buildResponse(message, HttpStatus.UNAUTHORIZED, null);
    }

    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        return buildResponse(message, status, null);
    }

    public static ResponseEntity<Object> error(String message, int status) {
        var httpStatus = HttpStatus.valueOf(status);
        return buildResponse(message, httpStatus, null);
    }

    private static ResponseEntity<Object> buildResponse(String message, HttpStatus status, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return new ResponseEntity<>(response, status);
    }
}
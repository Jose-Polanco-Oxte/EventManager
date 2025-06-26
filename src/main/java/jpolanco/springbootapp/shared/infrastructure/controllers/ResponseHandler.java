package jpolanco.springbootapp.shared.infrastructure.controllers;

import jpolanco.springbootapp.event.application.utils.Changes;
import jpolanco.springbootapp.shared.domain.DomainError;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.EntityCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ResponseHandler {

    public static <Payload, D extends Dto> ResponseEntity<?> resultToResponse(Result<Payload> result, DtoCreator<Payload, D> creator) {
        if (result.isFailure()) {
            var error = result.getError();
            if (error instanceof DomainError e) {
                return buildResponseFailure(e.getField(), e.getMessage(), e.getCode(), e.getDetails());
            } else {
                return buildResponseFailure(error.getField(), error.getMessage(), error.getCode(), null);
            }
        } else {
            var dto = creator.create(result.getValue());
            if (dto instanceof EntityCollection collection && collection.noContent()) {
                return noContent();
            }
            return ok(dto);
        }
    }

    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        return buildResponse(message, status, null);
    }

    public static ResponseEntity<?> reportToResponse(Report report) {
        if (report.isFailure()) {
            var errors = report.getErrors();
            return buildReportErrors(errors);
        } else {
            var changes = report.getChanges();
            return buildReportChanges(changes);
        }
    }

    public static <Payload, D extends Dto> ResponseEntity<?> optionalToResponse(Optional<Payload> data, DtoCreator<Payload, D> creator) {
        if (data.isEmpty()) {
            return notFound();
        } else {
            return ok(creator.create(data.get()));
        }
    }
    
    public static ResponseEntity<Object> simpleResponse(String message) {
        return buildResponse(message, HttpStatus.OK, null);
    }

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

    public static ResponseEntity<Object> error(String message, int status) {
        var httpStatus = HttpStatus.valueOf(status);
        return buildResponse(message, httpStatus, null);
    }

    private static ResponseEntity<Object> buildResponse(String message, HttpStatus status, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        if (data != null) {
            response.put("data", data);
        }
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<?> buildReportErrors(List<Error> errors) {
        Map<String, String> response = new HashMap<>();
        for (Error error : errors) {
            if (error instanceof DomainError domainError) {
                response.put(domainError.getField(), domainError.getMessage());
            } else {
                response.put(error.getField(), error.getMessage());
            }
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> buildReportChanges(List<Changes<?>> changes) {
        Map<String, Change<?>> response = new HashMap<>();
        for (Changes<?> change : changes) {
            response.put(change.fieldName(), new Change<>(change.oldValue(), change.newValue()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public record Change<T>(
            T before,
            T after
    ){}

    private static ResponseEntity<?> buildResponseFailure(String fieldName, String message, int code, String details) {
        Map<String, Object> response = new HashMap<>();
        if (fieldName != null) {
            response.put("Field", fieldName);
        }
        response.put("Message", message);
        if (details != null) {
            response.put("Details", details);
        }
        response.put("Timestamp", System.currentTimeMillis());
        response.put("Status", HttpStatus.valueOf(code));
        return new ResponseEntity<>(response, HttpStatus.valueOf(code));
    }
}
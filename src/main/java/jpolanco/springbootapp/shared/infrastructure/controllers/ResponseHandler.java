package jpolanco.springbootapp.shared.infrastructure.controllers;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;
import jpolanco.springbootapp.shared.infrastructure.dto.response.ChangesResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.ErrorResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SimpleResponse;
import jpolanco.springbootapp.shared.infrastructure.errors.BusinessRuleException;
import jpolanco.springbootapp.shared.utils.SuperResult;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.ComposedDtoCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class ResponseHandler {

    /* Result handlers */
    public static <Payload, D extends Dto> ResponseEntity<D> handleResult(Result<Payload> result, DtoCreator<Payload, D> creator) {
        if (result == null || creator == null) {
            throw new ResponseHandlerException("Result or creator cannot be null");
        }
        if (result.isFailure()) {
            throw new BusinessRuleException(result.getError());
        } else {
            if (result.dataIsNull()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                var dto = creator.create(result.getValue());
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }
        }
    }

    public static <Payload, D extends Dto> ResponseEntity<D> handleResult(Result<Payload> result, DtoCreator<Payload, D> creator, HttpStatus status) {
        if (result == null || creator == null || status == null) {
            throw new ResponseHandlerException("Result or creator cannot be null");
        }
        if (result.isFailure()) {
            throw new BusinessRuleException(result.getError());
        } else {
            if (result.dataIsNull()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                var dto = creator.create(result.getValue());
                return new ResponseEntity<>(dto, status);
            }
        }
    }

    public static ResponseEntity<SimpleResponse> handleVoidResult(Result<Void> result, String message) {
        if (result == null || message == null) {
            throw new ResponseHandlerException("Result or message cannot be null");
        }
        if (result.isFailure()) {
            throw new BusinessRuleException(result.getError());
        } else {
            return new ResponseEntity<>(new SimpleResponse(message), HttpStatus.OK);
        }
    }

    public static <T, D extends Dto> ResponseEntity<D> handleSuperResult(SuperResult<T, Report> result, DtoCreator<T, D> creator, HttpStatus status) {
        if (result == null || creator == null || status == null) {
            throw new ResponseHandlerException("Result, creator or status cannot be null");
        }
        if (result.isFailure()) {
            throw new BusinessRuleException(result.getFailure().getErrors());
        } else {
            var dto = creator.create(result.getSuccess());
            return new ResponseEntity<>(dto, status);
        }
    }

    /* Optional handler */

    public static <T, D extends Dto> ResponseEntity<D> handleOptional(Optional<T> optional, DtoCreator<T, D> creator) {
        if (optional == null || creator == null) {
            throw new ResponseHandlerException("Optional or creator cannot be null");
        }
        return optional.map(value -> new ResponseEntity<>(creator.create(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /* List handler */

    public static <E, D extends Dto> ResponseEntity<List<D>> handleList(List<E> list, DtoCreator<E, D> creator) {
        if (list == null || creator == null) {
            throw new ResponseHandlerException("List or creator cannot be null");
        }
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(list.stream().map(creator::create).toList(), HttpStatus.OK);
        }
    }

    /* Reports handlers */
    public static ResponseEntity<ChangesResponse> handleUpdateReport(UpdateReport report, String message) {
        if (report == null || message == null) {
            throw new ResponseHandlerException("Report or message cannot be null");
        }
        if (report.isFailure()) {
            throw new BusinessRuleException(report.getErrors());
        } else {
            var changes = report.getChanges();
            return changes != null && !changes.isEmpty()
                    ? ResponseEntity.ok(new ChangesResponse(message, changes))
                    : ResponseEntity.noContent().build();
        }
    }

    /* Collection handlers */

    public static <
            Entity,
            Input extends EntityCollection,
            EntityDto extends Dto,
            Creator extends DtoCreator<Entity, EntityDto>,
            Response extends Dto>
    ResponseEntity<Response> handleCollection(
            Input input,
            ComposedDtoCreator<Entity, Input, EntityDto, Creator, Response> composedCreator,
            Creator creator) {
        if (input == null || composedCreator == null || creator == null) {
            throw new ResponseHandlerException("Input, composedCreator or creator cannot be null");
        }
        return input.hasContent()
                ? ResponseEntity.ok(composedCreator.create(input, creator))
                : ResponseEntity.noContent().build();
    }

    public static <
            Input extends EntityCollection,
            Response extends Dto>
    ResponseEntity<Response> handleCollectionMapper(
            Input input,
            Function<Input, Response> mapper) {
        if (input == null || mapper == null) {
            throw new ResponseHandlerException("Input or mapper cannot be null");
        }
        return input.hasContent()
                ? ResponseEntity.ok(mapper.apply(input))
                : ResponseEntity.noContent().build();
    }

    /* Error handlers */

    public static ResponseEntity<ErrorResponse> handleError(String field, String message, int code, String details) {
        if (field == null || message == null || code <= 0) {
            throw new ResponseHandlerException("Field, message or code cannot be null or invalid");
        }
        return new ResponseEntity<>(new ErrorResponse(field, message, code, details), HttpStatus.valueOf(code));
    }

    public static ResponseEntity<List<ErrorResponse>> handleErrors(List<Error> errors) {
        if (errors == null || errors.isEmpty()) {
            throw new ResponseHandlerException("Errors list cannot be null or empty");
        }
        List<ErrorResponse> errorResponses = errors.stream()
                .map(error ->
                        new ErrorResponse(error.getField(), error.getMessage(), error.getCode(),
                                (error instanceof DomainError e) ? e.getDetails() : null))
                .toList();
        return new ResponseEntity<>(errorResponses, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /* Builds */

    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        return buildResponse(message, status, null);
    }

    public static <Payload, D extends Dto> ResponseEntity<?> optionalToResponse(Optional<Payload> data, DtoCreator<Payload, D> creator) {
        return data.map(payload -> ok(creator.create(payload))).orElseGet(ResponseHandler::notFound);
    }

    public static ResponseEntity<?> simpleResponse(String message) {
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
        response.put("Status", status.value());
        response.put("Message", message);
        response.put("Timestamp", System.currentTimeMillis());
        if (data != null) {
            response.put("Data", data);
        }
        return new ResponseEntity<>(response, status);
    }
}
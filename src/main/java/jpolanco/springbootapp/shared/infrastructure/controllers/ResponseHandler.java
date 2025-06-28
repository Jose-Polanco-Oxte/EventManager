package jpolanco.springbootapp.shared.infrastructure.controllers;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.DtoCreator;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;
import jpolanco.springbootapp.shared.infrastructure.dto.response.ChangesResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.ErrorResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SimpleResponse;
import jpolanco.springbootapp.shared.infrastructure.errors.BusinessRuleException;
import jpolanco.springbootapp.shared.utils.Either;
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

    public static ResponseEntity<SimpleResponse> handleVoidResult(Result<Void> result, String message) {
        if (result.isFailure()) {
            throw new BusinessRuleException(result.getError());
        } else {
            return new ResponseEntity<>(new SimpleResponse(message), HttpStatus.OK);
        }
    }

    public static <T, D extends Dto> ResponseEntity<D> handleEither(Either<T, List<Error>> either, DtoCreator<T, D> creator, HttpStatus status) {
        if (either.isRight()) {
            throw new BusinessRuleException(either.getRight());
        } else {
            var dto = creator.create(either.getLeft());
            return new ResponseEntity<>(dto, status);
        }
    }

    public static <Payload, D extends Dto> ResponseEntity<D> handleResult(Result<Payload> result, DtoCreator<Payload, D> creator) {
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

    public static <T, D extends Dto> ResponseEntity<D> handleOptional(Optional<T> optional, DtoCreator<T, D> creator) {
        return optional.map(value -> new ResponseEntity<>(creator.create(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public static <E, D extends Dto> ResponseEntity<List<D>> handleList(List<E> list, DtoCreator<E, D> creator) {
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(list.stream().map(creator::create).toList(), HttpStatus.OK);
        }
    }

    public static ResponseEntity<ErrorResponse> handleError(String field, String message, int code, String details) {
        return new ResponseEntity<>(new ErrorResponse(field, message, code, details), HttpStatus.valueOf(code));
    }

    public static ResponseEntity<List<ErrorResponse>> handleErrors(List<Error> errors) {
        List<ErrorResponse> errorResponses = errors.stream()
                .map(error ->
                        new ErrorResponse(error.getField(), error.getMessage(), error.getCode(),
                                (error instanceof DomainError e) ? e.getDetails() : null))
                .toList();
        return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ChangesResponse> handleReport(Report report, String message) {
        if (report.isFailure()) {
            throw new BusinessRuleException(report.getErrors());
        } else {
            var changes = report.getChanges();
            return changes != null && !changes.isEmpty()
                    ? ResponseEntity.ok(new ChangesResponse(message, changes))
                    : ResponseEntity.noContent().build();
        }
    }

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

        return input.hasContent()
                ? ResponseEntity.ok(composedCreator.create(input, creator))
                : ResponseEntity.noContent().build();
    }

    public static <
            Input extends EntityCollection,
            Response extends Dto>
    ResponseEntity<Response> handleMapper(
            Input input,
            Function<Input, Response> mapper) {

        return input.hasContent()
                ? ResponseEntity.ok(mapper.apply(input))
                : ResponseEntity.noContent().build();
    }


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
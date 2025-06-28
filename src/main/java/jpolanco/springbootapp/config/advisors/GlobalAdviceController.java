package jpolanco.springbootapp.config.advisors;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jpolanco.springbootapp.config.errors.SecurityAuth;
import jpolanco.springbootapp.event.infrastructure.errors.EventIntegrity;
import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.infrastructure.errors.BusinessRuleException;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.user.application.errors.IllegalUserOperation;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalAdviceController {
    // This class is used to handle global exceptions and provide a consistent response format
    // for all controllers in the application. It can be extended to include specific exception
    // handling methods as needed.

    private static final Logger logger = LoggerFactory.getLogger(GlobalAdviceController.class);

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<?> handleBusinessRuleException(BusinessRuleException e) {
        logger.error("Business rule violation: {}", e.getMessage(), e);
        if (e.getErrors().size() == 1) {
            // If there's only one error, return it directly
            var error = e.getErrors().getFirst();
            return ResponseHandler.handleError(error.getField(), error.getMessage(), error.getCode(), (error instanceof DomainError d) ? d.getDetails() : null);
        } else {
            return ResponseHandler.handleErrors(e.getErrors());
        }
    }

    @ExceptionHandler(SecurityAuth.class)
    public ResponseEntity<Object> handleSecurityAuthException(SecurityAuth e) {
        logger.error("Security authentication error: {}", e.getMessage(), e);
        return ResponseHandler.error(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", "Validation error occurred");
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            // Extraer el nombre del parámetro, como "page" o "size"
            String path = violation.getPropertyPath().toString(); // e.g. "getEventsByPages.page"
            String[] parts = path.split("\\.");
            String field = parts[parts.length - 1];

            errors.put(field, violation.getMessage());
        }
        errors.put("status", "400");
        logger.error("Constraint violation occurred: {}", errors, e);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        assert e.getRequiredType() != null;
        return ResponseHandler.badRequest(
                "Invalid request parameter: " + e.getName() + " should be invoke type " + e.getRequiredType().getSimpleName()
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e) {
        logger.error("JWT token expired: {}", e.getMessage(), e);
        String response = "JWT token expired";
        return ResponseHandler.unauthorized(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        logger.error("Missing request parameter: {}", e.getMessage(), e);
        String response = "Missing request parameter: " + e.getParameterName();
        return ResponseHandler.badRequest(response);
    }

    @ExceptionHandler(EventIntegrity.class)
    public ResponseEntity<Object> handleEventIntegrity(EventIntegrity e) {
        logger.error("Event integrity error: {}", e.getMessage(), e);
        String response = "Event integrity error: " + e.getMessage();
        return ResponseHandler.conflict(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        String response = "Invalid request body: " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseHandler.badRequest(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        logger.error("HTTP method not supported: {}", e.getMessage(), e);
        String response = "HTTP method not supported: " + e.getMessage();
        return ResponseHandler.methodNotAllowed(response);
    }

    @ExceptionHandler(IllegalUserOperation.class)
    public ResponseEntity<Object> handleIllegalUserOperation(IllegalUserOperation e) {
        logger.error("Illegal user operation: {}", e.getMessage(), e);
        String response = "Illegal user operation: " + e.getMessage();
        return ResponseHandler.badRequest(response);
    }

    @ExceptionHandler(UserIntegrity.class)
    public ResponseEntity<Object> handleDataFailure(UserIntegrity e) {
        logger.error("Data failure occurred: {}", e.getMessage(), e);
        String response = "Data failure occurred: " + e.getMessage();
        return ResponseHandler.serverError(response);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<Object> handleAuthenticationServiceException(AuthenticationServiceException e) {
        logger.error("Authentication service exception: {}", e.getMessage(), e);
        String response = e.getMessage();
        return ResponseHandler.unauthorized(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
        logger.error("Bad credentials: {}", e.getMessage(), e);
        String response = "Invalid email or password";
        return ResponseHandler.unauthorized(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("HTTP message not readable: {}", e.getMessage(), e);
        String response = "Invalid request body";
        return ResponseHandler.badRequest(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        logger.error("Null pointer exception occurred: {}", e.getMessage(), e);
        String response = "Null pointer exception occurred";
        return ResponseHandler.serverError(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException e) {
        logger.error("Resource not found: {}", e.getMessage(), e);
        return ResponseHandler.notFound();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Illegal argument exception: {}", e.getMessage(), e);
        String response = "Illegal argument exception occurred: " + e.getMessage();
        return ResponseHandler.badRequest(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        String response = "An unexpected error occurred";
        return ResponseHandler.serverError(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime exception occurred: {}", e.getMessage(), e);
        String response = "Runtime exception occurred";
        return ResponseHandler.serverError(response);
    }
}

package jpolanco.springbootapp.shared.infrastructure.advisors;

import jpolanco.springbootapp.event.application.errors.EventPersistenceFailure;
import jpolanco.springbootapp.event.infrastructure.errors.EventIntegrity;
import jpolanco.springbootapp.user.application.errors.IllegalUserOperation;
import jpolanco.springbootapp.user.application.errors.UserDataNotFound;
import jpolanco.springbootapp.user.infrastructure.errors.DataFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@ControllerAdvice
public class GlobalAdviceController {
    // This class is used to handle global exceptions and provide a consistent response format
    // for all controllers in the application. It can be extended to include specific exception
    // handling methods as needed.

    private static final Logger logger = LoggerFactory.getLogger(GlobalAdviceController.class);


    @ExceptionHandler(EventPersistenceFailure.class)
    public ResponseEntity<String> handleEventPersistenceFailure(EventPersistenceFailure e) {
        logger.error("Event persistence failure: {}", e.getMessage(), e);
        String response = "Event persistence failure: " + e.getMessage();
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(EventIntegrity.class)
    public ResponseEntity<String> handleEventIntegrity(EventIntegrity e) {
        logger.error("Event integrity error: {}", e.getMessage(), e);
        String response = "Event integrity error: " + e.getMessage();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        String response = "Invalid request body: " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        String response = e.getMessage();
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(UserDataNotFound.class)
    public ResponseEntity<String> handleUserDataNotFound(UserDataNotFound e) {
        logger.error(e.getMessage(), e);
        String response = e.getMessage();
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(IllegalUserOperation.class)
    public ResponseEntity<String> handleIllegalUserOperation(IllegalUserOperation e) {
        logger.error("Illegal user operation: {}", e.getMessage(), e);
        String response = "Illegal user operation: " + e.getMessage();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataFailure.class)
    public ResponseEntity<String> handleDataFailure(DataFailure e) {
        logger.error("Data failure occurred: {}", e.getMessage(), e);
        String response = "Data failure occurred: " + e.getMessage();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<String> handleAuthenticationServiceException(AuthenticationServiceException e) {
        logger.error("Authentication service exception: {}", e.getMessage(), e);
        String response = e.getMessage();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        logger.error("Bad credentials: {}", e.getMessage(), e);
        String response = "Invalid email or password";
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("HTTP message not readable: {}", e.getMessage(), e);
        String response = "Invalid request body";
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        logger.error("Null pointer exception occurred: {}", e.getMessage(), e);
        String response = "Null pointer exception occurred";
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e) {
        logger.error("Resource not found: {}", e.getMessage(), e);
        String response = "Resource not found";
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Illegal argument exception: {}", e.getMessage(), e);
        String response = "Illegal argument exception occurred: " + e.getMessage();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        String response = "An unexpected error occurred";
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime exception occurred: {}", e.getMessage(), e);
        String response = "Runtime exception occurred";
        return ResponseEntity.status(500).body(response);
    }
}

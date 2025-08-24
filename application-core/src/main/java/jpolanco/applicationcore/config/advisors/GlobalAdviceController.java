package jpolanco.applicationcore.config.advisors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.zxing.WriterException;
import jakarta.servlet.ServletException;
import jpolanco.applicationcore.config.advisors.erroserialize.DetailErrorResponse;
import jpolanco.applicationcore.config.advisors.erroserialize.DetailListErrorsResponse;
import jpolanco.applicationcore.config.exceptions.AuthException;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.exceptions.DomainExceptionHandler;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainErrType;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.user.infrastructure.exceptions.UserDataConflictException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalAdviceController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalAdviceController.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // Log the exception details
        logger.error("HTTP message not readable error occurred: {}", e.getMessage(), e);
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException ife) {
            String targetType = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown";
            String invalidValue = ife.getValue() != null ? ife.getValue().toString() : "null";
            String message = String.format("Invalid value '%s' for type '%s'.", invalidValue, targetType);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(message));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Malformed JSON request. Please check the request body and try again."));
        }
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthException(InternalAuthenticationServiceException e) {
        // Log the exception details
        logger.error("Internal authentication error occurred: {}", e.getMessage(), e);

        if (e.getCause() instanceof AuthException authEx) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(authEx.getMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Authentication failed. Please check your credentials and try again."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        // Log the exception details
        logger.error("Bad credentials error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid email or password. Please try again."));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        // Log the exception details
        logger.error("Authentication error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserDataConflictException.class)
    public ResponseEntity<ErrorResponse> handleUserDataConflictException(UserDataConflictException e) {
        // Log the exception details
        logger.error("User data conflict error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ApplicationExceptionHandler.class)
    public ResponseEntity<DetailListErrorsResponse> handleApplicationException(ApplicationExceptionHandler e) {
        // Log the application exception details
        logger.error("Application error occurred: {}", e.getMessage(), e);

        List<ServiceError> errors = e.getErrors();
        if (errors == null || errors.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }

        if (errors.size() == 1 && errors.getFirst().getType().status().equals(HttpStatus.NOT_FOUND)) {
            return ResponseEntity.notFound().build();
        }

        HttpStatus status = errors.size() == 1
                ? errors.getFirst().getType().status()
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new DetailListErrorsResponse(
                errors.stream()
                        .filter(err -> !err.getType().status().equals(HttpStatus.NOT_FOUND))
                        .map(err -> new DetailErrorResponse(
                                err.getField().orElse(null),
                                err.getMessage().orElse(null)
                        )).toList(),
                Instant.now()
        ));
    }

    @ExceptionHandler(DomainExceptionHandler.class)
    public ResponseEntity<DetailListErrorsResponse> handleDomainException(DomainExceptionHandler e) {
        // Log the domain exception details
        logger.error("Domain error occurred: {}", e.getMessage(), e);

        List<DomainError> errors = e.getErrors();
        if (errors == null || errors.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }

        HttpStatus status = errors.size() == 1
                ? errors.getFirst().getType() == DomainErrType.VALIDATION_ERROR ? HttpStatus.UNPROCESSABLE_ENTITY : HttpStatus.FORBIDDEN
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(new DetailListErrorsResponse(
                errors.stream()
                        .map(err -> new DetailErrorResponse(
                                err.getField().orElse(null),
                                err.getMessage().orElse(null)
                        )).toList(),
                Instant.now()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        // Log the exception details
        logger.error("Illegal argument error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An internal server error occurred. Please try again later."));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        // Log the exception details
        logger.error("No such element error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An internal server error occurred. Please try again later."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        // Log the exception details
        logger.error("Illegal state error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An internal server error occurred. Please try again later."));
    }

    @ExceptionHandler({IOException.class, WriterException.class})
    public ResponseEntity<ErrorResponse> handleIOException(IOException e, WriterException we) {
        // Decide which exception to log based on which one is not null
        if (e != null) {
            logger.error("IO error occurred: {}", e.getMessage(), e);
        } else if (we != null) {
            logger.error("Writer error occurred: {}", we.getMessage(), we);
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An internal server error occurred. Please try again later."));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> handleServletException(ServletException e) {
        // Log the servlet exception details
        logger.error("Servlet error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("A servlet error occurred. Please try again later."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        // Log the runtime exception details
        logger.error("A runtime error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("A runtime error occurred. Please try again later."));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // Log the exception details
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("An unexpected error occurred. Please try again later."));
    }

    public record ErrorResponse(String message, Instant timestamp) {
        public ErrorResponse(String message) {
            this(message, Instant.now());
        }
    }
}

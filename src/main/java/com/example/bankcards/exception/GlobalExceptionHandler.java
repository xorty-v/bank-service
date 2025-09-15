package com.example.bankcards.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Centralized exception handler for the application.
 * Handles various exceptions globally and returns standardized error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Standard error response structure.
     *
     * @param message   the error message
     * @param status    HTTP status code
     * @param timestamp timestamp when the error occurred
     */
    public record ErrorResponse(String message, HttpStatus status, LocalDateTime timestamp) {}

    /**
     * Handles illegal arguments and missing elements.
     *
     * @param e exception thrown
     * @return HTTP 400 Bad Request with error details
     */
    @ExceptionHandler({IllegalArgumentException.class, java.util.NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        logger.warn("Bad request: {}", e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors for @Valid annotated request bodies.
     *
     * @param e MethodArgumentNotValidException thrown on validation failure
     * @return HTTP 400 Bad Request with all validation errors concatenated
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.warn("Validation failed: {}", errors);
        return buildResponse("Validation error(s): " + errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles access denied exceptions thrown by Spring Security.
     *
     * @param e AccessDeniedException thrown when user lacks permissions
     * @return HTTP 403 Forbidden with message "Access denied"
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
        logger.warn("Access denied: {}", e.getMessage());
        return buildResponse("Access denied", HttpStatus.FORBIDDEN);
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * @param e the exception
     * @return HTTP 500 Internal Server Error with message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        logger.error("Internal server error", e);
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Builds a standardized ResponseEntity with the given message and status.
     *
     * @param message error message
     * @param status  HTTP status code
     * @return ResponseEntity containing ErrorResponse
     */
    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(message, status, LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }
}

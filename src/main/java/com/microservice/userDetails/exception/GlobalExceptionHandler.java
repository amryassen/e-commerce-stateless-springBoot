package com.microservice.userDetails.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
// ResponseEntityExceptionHandler is a base class for handling exceptions in Spring MVC applications.
// It provides a convenient way to handle exceptions and return appropriate HTTP responses.
// The @ControllerAdvice annotation allows you to handle exceptions globally across all controllers.
// By extending ResponseEntityExceptionHandler, you can customize the exception handling behavior
// for your application, such as returning specific error messages or status codes for different types of exceptions
// without having to write repetitive code in each controller.
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage(), ex);
        // This method handles validation errors that occur when a request body does not match the expected format
        // or when validation constraints are violated.
        // It can be used to return a custom error response with details about the validation errors.
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        log.error("Validation errors: {}", errors);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed for one or more arguments.");
        body.put("errors", errors);
        body.put("status", status.value());
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        // This method handles RuntimeExceptions thrown by the application.
        // You can customize the response here, such as logging the exception or returning a specific error message.
        Map<String, Object> body = new HashMap<>();
        body.put("message", "An unexpected error occurred.");
        body.put("error", ex.getMessage());
        body.put("status", 500);
        body.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(body, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Where to use:
     * - In any secured controller or service method when authentication fails.
     * - Example for admin-only APIs:
     * if (!currentUser.isAdmin()) {
     * throw new UnauthorizedException("Admin privileges required");
     * }
     * - Example for authenticated endpoints:
     * if (currentUser == null) {
     * throw new UnauthorizedException("You must be logged in to access this resource");
     * }
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> unauthorizedException(AccessDeniedException ex) {
        log.error("Unauthorized exception occurred: {}", ex.getMessage(), ex);
        // This method handles unauthorized access exceptions.
        // You can customize the response here, such as returning a specific error message or status code.
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Unauthorized access.");
        body.put("error", ex.getMessage());
        body.put("status", 401);
        body.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(body, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "User Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

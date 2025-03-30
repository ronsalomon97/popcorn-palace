package com.att.tdp.popcorn_palace.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice  // Use RestControllerAdvice instead of ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Unified error response builder
    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            Exception ex, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        request.getDescription(false),
                        LocalDateTime.now()
                ),
                status
        );
    }

    // Handle not found errors
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundExceptions(
            Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    // Handle conflict errors
    @ExceptionHandler({
        DuplicateMovieTitleException.class,
        OverlappingShowtimeException.class, 
        SeatAlreadyBookedException.class,
        MovieHasShowtimesException.class
    })
    public ResponseEntity<ApiErrorResponse> handleConflictExceptions(
            Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    // Handle validation errors with detailed field errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                request.getDescription(false),
                LocalDateTime.now()
        );
        errorResponse.setFieldErrors(fieldErrors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle bad request errors
    @ExceptionHandler({
        InvalidShowtimeDurationException.class,
        HttpMessageNotReadableException.class,
        IllegalArgumentException.class,
        NumberFormatException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequestExceptions(
            Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    // Catch-all handler for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUncaughtExceptions(
            Exception ex, WebRequest request) {
        logger.error("Uncaught exception", ex);
        return buildErrorResponse(
                new RuntimeException("An unexpected error occurred"), 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                request
        );
    }
} 
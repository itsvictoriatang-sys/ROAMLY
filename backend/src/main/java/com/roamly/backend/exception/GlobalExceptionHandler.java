package com.roamly.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(
        EmailAlreadyExistsException exception) {

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of(
                    "error", exception.getMessage()
            ));
}

@ExceptionHandler(InvalidCredentialsException.class)
public ResponseEntity<Map<String, String>> handleInvalidCredentials(
        InvalidCredentialsException exception) {

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of(
                    "error", exception.getMessage()
            ));
}
@ExceptionHandler(InvalidTripDateException.class)
public ResponseEntity<Map<String, String>> handleInvalidTripDate(
        InvalidTripDateException exception) {

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of(
                    "error", exception.getMessage()
            ));
}
    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException exception) {

    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
}
@ExceptionHandler(TripNotFoundException.class)
public ResponseEntity<Map<String, String>> handleTripNotFound(
        TripNotFoundException ex) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<Map<String, String>> handleUserNotFound(
        UserNotFoundException ex) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(ForbiddenTripActionException.class)
public ResponseEntity<Map<String, String>> handleForbiddenTripAction(
        ForbiddenTripActionException ex) {

    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(TripMemberAlreadyExistsException.class)
public ResponseEntity<Map<String, String>> handleTripMemberAlreadyExists(
        TripMemberAlreadyExistsException ex) {

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
}
@ExceptionHandler(InvalidTripRoleException.class)
public ResponseEntity<Map<String, String>> handleInvalidTripRole(
        InvalidTripRoleException ex) {

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", ex.getMessage()));
}
@ExceptionHandler(InvalidItineraryTimeException.class)
public ResponseEntity<Map<String, String>> handleInvalidItineraryTime(
        InvalidItineraryTimeException ex) {

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", ex.getMessage()));
}
}
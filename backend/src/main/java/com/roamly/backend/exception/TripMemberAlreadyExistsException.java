package com.roamly.backend.exception;

public class TripMemberAlreadyExistsException extends RuntimeException {

    public TripMemberAlreadyExistsException(String message) {
        super(message);
    }
}
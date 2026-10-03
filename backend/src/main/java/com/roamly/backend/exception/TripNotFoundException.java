package com.roamly.backend.exception;

public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException(String message) {
        super(message);
    }
}
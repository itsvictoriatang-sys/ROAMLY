package com.roamly.backend.exception;

public class InvalidItineraryTimeException extends RuntimeException {

    public InvalidItineraryTimeException(String message) {
        super(message);
    }
}
package com.example.restapi.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException() {
    }

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}

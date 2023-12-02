package com.example.restapi.exception;

public class AppointmentCompletionTooEarlyException extends RuntimeException {
    public AppointmentCompletionTooEarlyException() {
    }

    public AppointmentCompletionTooEarlyException(String message) {
        super(message);
    }
}

package com.example.restapi.exception;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException() {
    }

    public DoctorNotFoundException(String message) {
        super(message);
    }
}

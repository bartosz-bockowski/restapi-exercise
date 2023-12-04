package com.example.restapi.exception;

public class UserLockedException extends RuntimeException {

    public UserLockedException() {
    }

    public UserLockedException(String message) {
        super(message);
    }

}

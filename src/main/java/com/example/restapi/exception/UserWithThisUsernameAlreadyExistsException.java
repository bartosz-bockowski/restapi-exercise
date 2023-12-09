package com.example.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserWithThisUsernameAlreadyExistsException extends RuntimeException {
    public UserWithThisUsernameAlreadyExistsException() {
    }

    public UserWithThisUsernameAlreadyExistsException(String message) {
        super(message);
    }
}

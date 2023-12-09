package com.example.restapi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserWithThisUsernameAndPeselAlreadyExistsException extends RuntimeException {
    public UserWithThisUsernameAndPeselAlreadyExistsException() {
    }

    public UserWithThisUsernameAndPeselAlreadyExistsException(String message) {
        super(message);
    }
}

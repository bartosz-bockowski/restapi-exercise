package com.example.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserWithThisPeselAlreadyExistsException extends RuntimeException {
    public UserWithThisPeselAlreadyExistsException() {
    }

    public UserWithThisPeselAlreadyExistsException(String message) {
        super(message);
    }
}

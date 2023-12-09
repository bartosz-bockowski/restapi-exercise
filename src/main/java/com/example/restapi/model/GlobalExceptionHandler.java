package com.example.restapi.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map((error) ->
                        error.getField()
                                .concat(": ")
                                .concat(Optional.ofNullable(error.getDefaultMessage()).orElse("")))
                .toList();
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(UserWithThisPeselAlreadyExistsException.class)
//    public ResponseEntity<String> handlePeselTaken(UserWithThisPeselAlreadyExistsException e){
//        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleInvalidDataException(RuntimeException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}

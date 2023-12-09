package com.example.restapi.config;

import com.example.restapi.exception.*;
import com.example.restapi.model.ApiError;
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

    //invalid data
    @ExceptionHandler({
            UserWithThisUsernameAlreadyExistsException.class,
            UserWithThisPeselAlreadyExistsException.class,
            UserWithThisUsernameAndPeselAlreadyExistsException.class,
            AppointmentCompletionTooEarlyException.class
    })
    public ResponseEntity<ApiError> handleInvalidDataException(RuntimeException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    //not found
    @ExceptionHandler({
            UserNotFoundException.class,
            DoctorNotFoundException.class,
            PatientNotFoundException.class,
            AppointmentNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    //access denied
    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<ApiError> handleAccessDeniedException(RuntimeException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    //validation
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

    //validation utils
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}

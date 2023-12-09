package com.example.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiError {

    private HttpStatus httpStatus;

    private Object errors;

}

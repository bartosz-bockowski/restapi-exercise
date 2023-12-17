package com.example.restapi.controller;

import com.example.restapi.model.AuthenticationRequest;
import com.example.restapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    @GetMapping("/jwtToken")
    public ResponseEntity<String> jwtToken(@RequestBody AuthenticationRequest userInput) {
        return new ResponseEntity<>(authenticationService.generateTokenFromUserInput(userInput), HttpStatus.OK);
    }
    
}

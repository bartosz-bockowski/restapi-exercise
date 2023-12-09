package com.example.restapi.service;

import com.example.restapi.domain.User;
import com.example.restapi.model.AuthenticationRequest;
import com.example.restapi.security.jwt.JwtService;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public String generateTokenFromUserInput(AuthenticationRequest userInput) {
        User user = userService.findByUserName(userInput.getUsername());
        if (!BCrypt.checkpw(userInput.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials!");
        }
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userInput.getUsername(), userInput.getPassword()));
        return jwtService.generateToken(user);
    }

}

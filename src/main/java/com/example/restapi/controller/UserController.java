package com.example.restapi.controller;

import com.example.restapi.command.PatientCommand;
import com.example.restapi.command.UserCommand;
import com.example.restapi.domain.Patient;
import com.example.restapi.domain.User;
import com.example.restapi.dto.PatientDTO;
import com.example.restapi.dto.UserDTO;
import com.example.restapi.security.jwt.JwtService;
import com.example.restapi.security.user.UserRepository;
import com.example.restapi.security.user.UserService;
import com.example.restapi.service.AdminService;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwt;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserCommand userCommand) {
        return new ResponseEntity<>(modelMapper
                .map(userService.saveUser(
                        modelMapper.map(userCommand, User.class)), UserDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/jwtToken")
    public ResponseEntity<String> jwtToken(@RequestBody User userInput){
        return new ResponseEntity<>(userService.generateTokenFromUserInput(userInput),HttpStatus.OK);
    }

    @PostMapping("/changeStatus/{userId}")
    public ResponseEntity<UserDTO> changeUserStatus(@PathVariable Long userId){
        return new ResponseEntity<>(modelMapper.map(adminService.changeUserStatusById(userId),UserDTO.class),HttpStatus.OK);
    }

}

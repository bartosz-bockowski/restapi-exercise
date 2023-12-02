package com.example.restapi.controller;

import com.example.restapi.command.PatientCommand;
import com.example.restapi.command.UserCommand;
import com.example.restapi.domain.Patient;
import com.example.restapi.domain.User;
import com.example.restapi.dto.PatientDTO;
import com.example.restapi.dto.UserDTO;
import com.example.restapi.security.user.UserRepository;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserCommand userCommand) {
        return new ResponseEntity<>(modelMapper
                .map(userService.saveUser(
                        modelMapper.map(userCommand, User.class)), UserDTO.class), HttpStatus.CREATED);
    }

}

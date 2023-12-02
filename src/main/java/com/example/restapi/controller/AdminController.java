package com.example.restapi.controller;

import com.example.restapi.command.AdminCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.dto.AdminDTO;
import com.example.restapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ModelMapper modelMapper;

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminDTO> save(@RequestBody AdminCommand adminCommand) {
        return new ResponseEntity<>(modelMapper
                .map(adminService.save(
                        modelMapper.map(adminCommand, Admin.class)), AdminDTO.class), HttpStatus.CREATED);
    }
}

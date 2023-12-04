package com.example.restapi.controller;

import com.example.restapi.command.AdminCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.AdminAction;
import com.example.restapi.dto.AdminActionDTO;
import com.example.restapi.dto.AdminDTO;
import com.example.restapi.dto.UserDTO;
import com.example.restapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/actions")
    public ResponseEntity<List<AdminActionDTO>> getAdminActions(){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return new ResponseEntity<>(adminService.getAdminActionsOfLoggedAdmin().stream()
                .map(action -> modelMapper.map(action, AdminActionDTO.class)).toList(),HttpStatus.OK);
    }

}

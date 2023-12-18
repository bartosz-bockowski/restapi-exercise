package com.example.restapi.controller;

import com.example.restapi.command.AdminCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.dto.AdminActionDTO;
import com.example.restapi.dto.AdminDTO;
import com.example.restapi.dto.UserDTO;
import com.example.restapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ModelMapper modelMapper;

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminDTO> save(@Valid @RequestBody AdminCommand adminCommand) {
        return new ResponseEntity<>(modelMapper
                .map(adminService.save(
                        modelMapper.map(adminCommand, Admin.class)), AdminDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/actions")
    public ResponseEntity<List<AdminActionDTO>> getAdminActions(@SortDefault("id") Pageable pageable) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return new ResponseEntity<>(adminService.getAdminActionsOfLoggedAdmin(pageable).stream()
                .map(action -> modelMapper.map(action, AdminActionDTO.class)).toList(), HttpStatus.OK);
    }

    @PostMapping("/changeStatus/{userId}")
    public ResponseEntity<UserDTO> changeUserStatus(@PathVariable Long userId) {
        return new ResponseEntity<>(modelMapper.map(adminService.changeUserStatusById(userId), UserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/switchLockedUser/{userId}")
    public ResponseEntity<UserDTO> switchLocked(@PathVariable Long userId) {
        return new ResponseEntity<>(modelMapper.map(adminService.switchLockedById(userId), UserDTO.class), HttpStatus.OK);
    }

}

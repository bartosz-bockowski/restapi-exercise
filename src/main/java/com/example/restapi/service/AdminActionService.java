package com.example.restapi.service;


import com.example.restapi.domain.AdminAction;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.repository.AdminActionRepository;
import com.example.restapi.security.user.UserService;
import liquibase.pro.packaged.A;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AdminActionService {

    private final AdminService adminService;

    private final AdminActionRepository adminActionRepository;

    private final UserService userService;

    public void createAndSaveAction(AdminActionType type){
        if(!userService.getLoggedUser().getUserType().equals("Admin")){
            return;
        }
        AdminAction action = new AdminAction();
        action.setType(type);
        adminActionRepository.save(action);
    }
}

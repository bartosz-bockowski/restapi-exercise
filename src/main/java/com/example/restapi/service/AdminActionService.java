package com.example.restapi.service;


import com.example.restapi.domain.AdminAction;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.repository.AdminActionRepository;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AdminActionService {

    private final AdminActionRepository adminActionRepository;

    private final UserService userService;

    public void createAndSaveAction(AdminActionType type) {
        if (!Objects.equals(userService.getLoggedUser().getUserType(), "Admin")) {
            throw new AccessDeniedException("Access denied!");
        }
        AdminAction action = new AdminAction();
        action.setType(type);
        adminActionRepository.save(action);
    }
}

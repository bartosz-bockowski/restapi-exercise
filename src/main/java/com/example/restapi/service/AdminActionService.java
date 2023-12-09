package com.example.restapi.service;


import com.example.restapi.domain.AdminAction;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.repository.AdminActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminActionService {

    private final AdminActionRepository adminActionRepository;

    public void createAndSaveAction(AdminActionType type) {
        AdminAction action = new AdminAction();
        action.setType(type);
        adminActionRepository.save(action);
    }
}

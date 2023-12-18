package com.example.restapi.service;


import com.example.restapi.domain.Admin;
import com.example.restapi.domain.AdminAction;
import com.example.restapi.domain.User;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.model.ListUtils;
import com.example.restapi.model.UserStatus;
import com.example.restapi.repository.AdminRepository;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    private final AdminActionService adminActionService;

    public Admin save(Admin admin) {
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public List<AdminAction> getAdminActionsOfLoggedAdmin(Pageable pageable) {
        User user = userService.getLoggedUser();
        if (!Objects.equals(user.getUserType(), "Admin")) {
            throw new AccessDeniedException("You're not an admin!");
        }
        return ListUtils.getListPartFromPageable(adminRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found!"))
                .getActions(), pageable);
    }

    public User changeUserStatusById(Long userId) {
        User user = userService.findById(userId);
        UserStatus newStatus = Objects.equals(user.getStatus(), UserStatus.ENABLED) ? UserStatus.LOCKED : UserStatus.ENABLED;
        adminActionService.createAndSaveAction(Objects.equals(newStatus, UserStatus.LOCKED) ? AdminActionType.LOCK_USER : AdminActionType.ENABLE_USER);
        user.setStatus(newStatus);
        return userService.updateUser(user);
    }

    public User switchLockedById(Long userId) {
        User user = userService.findById(userId);
        user.setLocked(!user.isLocked());
        adminActionService.createAndSaveAction(user.isLocked() ? AdminActionType.LOCK_USER : AdminActionType.ENABLE_USER);
        return userService.updateUser(user);
    }

}

package com.example.restapi.service;


import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Doctor;
import com.example.restapi.domain.User;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Admin findById(Long id){
        return adminRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
    }

    public Admin save(Admin admin) {
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
}

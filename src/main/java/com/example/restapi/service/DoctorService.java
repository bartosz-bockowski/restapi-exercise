package com.example.restapi.service;

import com.example.restapi.domain.Doctor;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.exception.DoctorNotFoundException;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.repository.DoctorRepository;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    private final AdminActionService adminActionService;

    public Doctor save(Doctor doctor) {
        doctor.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor cannot be found!"));
    }

    public List<Doctor> listAll() {
        return doctorRepository.findAll();
    }

    public void deleteById(Long id) {
        userService.deleteCheck(id);
        doctorRepository.delete(doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor cannot be found!")));
        adminActionService.createAndSaveAction(AdminActionType.DELETE_DOCTOR);
    }
}

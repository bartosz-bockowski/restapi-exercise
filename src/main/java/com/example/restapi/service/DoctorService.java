package com.example.restapi.service;

import com.example.restapi.domain.Doctor;
import com.example.restapi.exception.DoctorNotFoundException;
import com.example.restapi.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor cannot be found!"));
    }

    public List<Doctor> listAll() {
        return doctorRepository.findAll();
    }
}

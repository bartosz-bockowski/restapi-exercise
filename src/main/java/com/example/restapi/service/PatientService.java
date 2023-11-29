package com.example.restapi.service;

import com.example.restapi.domain.Patient;
import com.example.restapi.exception.PatientNotFoundException;
import com.example.restapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Doctor cannot be found!"));
    }

    public List<Patient> listAll() {
        return patientRepository.findAll();
    }

    public void deleteById(Long id) {
        patientRepository.delete(patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Doctor cannot be found!")));
    }
}

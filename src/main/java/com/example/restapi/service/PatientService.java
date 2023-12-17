package com.example.restapi.service;

import com.example.restapi.domain.Appointment;
import com.example.restapi.domain.Patient;
import com.example.restapi.exception.PatientNotFoundException;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.repository.PatientRepository;
import com.example.restapi.security.user.UserRepository;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    private final UserRepository userRepository;

    private final AdminActionService adminActionService;

    public Patient save(Patient patient) {
        patient.setPassword(bCryptPasswordEncoder.encode(patient.getPassword()));
        return userRepository.save(patient);
    }

    public Patient update(Patient patient) {
        return userRepository.save(patient);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient cannot be found!"));
    }

    public List<Patient> listAll() {
        return patientRepository.findAll();
    }

    public void deleteById(Long id) {
        userService.deleteCheck(id);
        patientRepository.delete(patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient cannot be found!")));
        adminActionService.createAndSaveAction(AdminActionType.DELETE_DOCTOR);
    }

    public List<Appointment> getAppointmentsOfLoggedPatient() {
        return Patient.class.cast(userService.getLoggedUser()).getAppointments();
    }
}

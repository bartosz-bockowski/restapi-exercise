package com.example.restapi.service;

import com.example.restapi.domain.Appointment;
import com.example.restapi.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(Long patientId) {
        return null;
    }

    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(Long doctorId) {
        return null;
    }
}

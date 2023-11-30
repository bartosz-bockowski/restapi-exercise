package com.example.restapi.controller;

import com.example.restapi.command.AppointmentCommand;
import com.example.restapi.domain.Appointment;
import com.example.restapi.dto.AppointmentDTO;
import com.example.restapi.repository.DoctorRepository;
import com.example.restapi.repository.PatientRepository;
import com.example.restapi.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AppointmentDTO> save(@RequestBody AppointmentCommand appointmentCommand){
        Appointment appointment = modelMapper.map(appointmentCommand,Appointment.class);
        appointment.setPatient(patientRepository.getReferenceById(appointmentCommand.getPatient()));
        appointment.setDoctor(doctorRepository.getReferenceById(3L));
        return new ResponseEntity<>(modelMapper.map(
                appointmentService.save(appointment), AppointmentDTO.class
        ), HttpStatus.OK);
    }
}

package com.example.restapi.controller;

import com.example.restapi.command.appointment.AppointmentCommand;
import com.example.restapi.domain.Appointment;
import com.example.restapi.domain.User;
import com.example.restapi.dto.AppointmentDTO;
import com.example.restapi.dto.DoctorAppointmentDTO;
import com.example.restapi.dto.PatientAppointmentDTO;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.repository.DoctorRepository;
import com.example.restapi.repository.PatientRepository;
import com.example.restapi.security.user.UserService;
import com.example.restapi.service.AppointmentService;
import com.example.restapi.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    @PostMapping
    public ResponseEntity<PatientAppointmentDTO> save(@RequestBody AppointmentCommand appointmentCommand){
        return new ResponseEntity<>(modelMapper.map(
                appointmentService.save(appointmentCommand), PatientAppointmentDTO.class
        ), HttpStatus.OK);
    }

    @PostMapping("/cancel/{appointmentId}")
    public ResponseEntity<?> cancel(@PathVariable Long appointmentId){
        return new ResponseEntity<>(modelMapper.map(appointmentService.cancelById(appointmentId),appointmentService.getDTOforLoggedUser()),HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<?>> my(){
        return new ResponseEntity<>(appointmentService.getAppointmentsOfLoggedUser(),HttpStatus.OK);
    }

    @PostMapping("/complete/{appointmentId}/{healthId}")
    public ResponseEntity<DoctorAppointmentDTO> complete(@PathVariable Long appointmentId, @PathVariable int healthId){
        return new ResponseEntity<>(modelMapper.map(appointmentService.completeById(appointmentId, healthId), DoctorAppointmentDTO.class),HttpStatus.OK);
    }
}

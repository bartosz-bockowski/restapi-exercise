package com.example.restapi.controller;


import com.example.restapi.command.DoctorCommand;
import com.example.restapi.domain.Doctor;
import com.example.restapi.dto.DoctorAppointmentDTO;
import com.example.restapi.dto.DoctorDTO;
import com.example.restapi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<DoctorDTO> save(@Valid @RequestBody DoctorCommand doctorCommand) {
        return new ResponseEntity<>(modelMapper
                .map(doctorService.save(
                        modelMapper.map(doctorCommand, Doctor.class)), DoctorDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(modelMapper
                .map(doctorService.findById(id), DoctorDTO.class), HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity<List<DoctorDTO>> all() {
        return new ResponseEntity<>(doctorService.listAll().stream()
                .map(doctor -> modelMapper
                        .map(doctor, DoctorDTO.class))
                .toList(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private HttpStatus delete(@PathVariable Long id) {
        doctorService.deleteById(id);
        return HttpStatus.OK;
    }

    @GetMapping("/myAppointments")
    public ResponseEntity<List<DoctorAppointmentDTO>> myAppointments(@SortDefault("id") Pageable pageable) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return new ResponseEntity<>(doctorService.getAppointmentsOfLoggedDoctor(pageable).stream()
                .map(appointment -> modelMapper.map(appointment, DoctorAppointmentDTO.class))
                .toList(), HttpStatus.OK);
    }

}

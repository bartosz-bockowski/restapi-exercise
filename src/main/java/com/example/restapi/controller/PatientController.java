package com.example.restapi.controller;

import com.example.restapi.command.PatientCommand;
import com.example.restapi.domain.Patient;
import com.example.restapi.dto.PatientDTO;
import com.example.restapi.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient")
public class PatientController {
    private final PatientService patientService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PatientDTO> save(@RequestBody PatientCommand patientCommand) {
        return new ResponseEntity<>(modelMapper
                .map(patientService.save(
                        modelMapper.map(patientCommand, Patient.class)), PatientDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(modelMapper
                .map(patientService.findById(id), PatientDTO.class), HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity<List<PatientDTO>> all() {
        return new ResponseEntity<>(patientService.listAll().stream()
                .map(patient -> modelMapper
                        .map(patient, PatientDTO.class))
                .toList(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<PatientDTO> delete(@PathVariable Long id) {
        patientService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK); //HttpStatus.NO_CONTENT?
    }
}

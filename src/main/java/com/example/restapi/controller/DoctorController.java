package com.example.restapi.controller;


import com.example.restapi.command.DoctorCommand;
import com.example.restapi.domain.Doctor;
import com.example.restapi.dto.DoctorDTO;
import com.example.restapi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    private final ModelMapper modelMapper;

    //todo zobacz jaka jest roznica miedzy PutMapping a PatchMapping, flyway, liquibase, modelmapper, mapstruct
//    do appointment stworzy @Post, stowrzmy rowniez metode aby Pacjent i doktor mogli wypisywac swoje wizyty (aktualnie za pomoca swojego ID
//    a potem sesji. Do tego dodac walidacje @Notblack etc. - zrobione dodawanie appointmentu, bez list, bez security, bez walidacji
//    mozliwosc zarejestrowania sie jako patient i doctor X
//    Poczytaj o @Inheritance X
//    @DiscriminatorColumn, discriminatorType X
//    dziedziczenie w springu X

    @PostMapping
    public ResponseEntity<DoctorDTO> save(@RequestBody DoctorCommand doctorCommand) {
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
}

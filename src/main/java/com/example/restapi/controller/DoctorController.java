package com.example.restapi.controller;


import com.example.restapi.command.DoctorCommand;
import com.example.restapi.domain.Doctor;
import com.example.restapi.dto.DoctorDTO;
import com.example.restapi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //todo zobacz jaka jest roznica miedzy PutMapping a PatchMapping, flyway, liquibase, modelmapper, mapstruct
//    do appointment stworzy @Post, stowrzmy rowniez metode aby Pacjent i doktor mogli wypisywac swoje wizyty (aktualnie za pomoca swojego ID
//    a potem sesji. Do tego dodac walidacje @Notblack etc.
//    mozliwosc zarejestrowania sie jako patient i doctor - zrobione
//    Poczytaj o @Inheritance - zrobione
//    @DiscriminatorColumn, discriminatorType - zrobione
//    dziedziczenie w springu - zrobione
//    https://www.youtube.com/watch?v=KxqlJblhzfI
    @PostMapping
    public ResponseEntity<DoctorDTO> save(@RequestBody DoctorCommand doctorCommand) {
        doctorCommand.setPassword(bCryptPasswordEncoder.encode(doctorCommand.getPassword()));
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
    private ResponseEntity<DoctorDTO> delete(@PathVariable Long id) {
        doctorService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK); //HttpStatus.NO_CONTENT?
    }
}

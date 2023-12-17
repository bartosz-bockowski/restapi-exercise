package com.example.restapi.dto;

import com.example.restapi.model.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Setter
public class PatientAppointmentDTO {

    private LocalDate date;

    private DoctorDTO doctorDTO;

    private AppointmentStatus status;
    
}

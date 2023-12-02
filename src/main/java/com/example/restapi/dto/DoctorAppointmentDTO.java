package com.example.restapi.dto;

import com.example.restapi.model.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Setter
public class DoctorAppointmentDTO {
    private LocalDate date;
    private PatientDTO patientDTO;
    private AppointmentStatus status;
}

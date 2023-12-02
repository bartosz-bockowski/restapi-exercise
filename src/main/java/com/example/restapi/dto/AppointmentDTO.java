package com.example.restapi.dto;

import com.example.restapi.domain.Patient;
import com.example.restapi.model.AppointmentStatus;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
public class AppointmentDTO {
    private LocalDate date;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private AppointmentStatus status;
}

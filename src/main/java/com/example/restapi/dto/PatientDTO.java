package com.example.restapi.dto;

import com.example.restapi.model.PatientHealthStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatientDTO extends UserDTO {

    private PatientHealthStatus health;

    private List<AppointmentDTO> appointments;

}

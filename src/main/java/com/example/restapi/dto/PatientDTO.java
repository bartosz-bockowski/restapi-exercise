package com.example.restapi.dto;

import com.example.restapi.model.PatientHealthStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatientDTO extends UserDTO {

    private PatientHealthStatus health;

}

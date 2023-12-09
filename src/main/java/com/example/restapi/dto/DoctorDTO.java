package com.example.restapi.dto;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DoctorDTO extends UserDTO {

    private DoctorSpecializationType specialization;

}

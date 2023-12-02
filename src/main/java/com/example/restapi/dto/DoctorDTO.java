package com.example.restapi.dto;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.Data;

@Data
public class DoctorDTO extends UserDTO {

    private DoctorSpecializationType specialization;

}

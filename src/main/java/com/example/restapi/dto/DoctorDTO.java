package com.example.restapi.dto;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.Data;

@Data
public class DoctorDTO {
    private String name;
    private String surname;
    private DoctorSpecializationType specialization;
    private int age;
}

package com.example.restapi.dto;

import com.example.restapi.domain.Appointment;
import com.example.restapi.model.DoctorSpecializationType;
import lombok.Data;

import javax.persistence.OneToMany;
import java.util.List;

@Data
public class DoctorDTO {
    private String name;
    private String surname;
    private DoctorSpecializationType specialization;
    private int age;
}

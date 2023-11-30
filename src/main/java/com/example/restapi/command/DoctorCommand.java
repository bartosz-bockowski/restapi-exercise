package com.example.restapi.command;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCommand {

    private String name;

    private String surname;

    private DoctorSpecializationType specialization;

    private int age;

    private String username;

    private String password;

}

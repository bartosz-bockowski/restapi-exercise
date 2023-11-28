package com.example.restapi.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCommand {

    private String name;

    private String surname;

    private int age;

    private String pesel;

}

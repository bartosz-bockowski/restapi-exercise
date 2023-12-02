package com.example.restapi.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommand {

    private String name;

    private String surname;

    private String userType;

    private int age;

    private String pesel;

    private String username;

    private String password;

}

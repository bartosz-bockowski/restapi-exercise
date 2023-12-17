package com.example.restapi.dto;

import com.example.restapi.model.UserStatus;
import lombok.Data;

@Data
public class UserDTO {

    private String name;

    private String surname;

    private int age;

    private String pesel;

    private String username;

    private UserStatus userStatus;

    private boolean locked;

}

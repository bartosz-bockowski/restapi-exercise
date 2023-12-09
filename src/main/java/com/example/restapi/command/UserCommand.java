package com.example.restapi.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommand {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Min(1)
    private int age;

    @Length(min = 11, max = 11)
    private String pesel;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}

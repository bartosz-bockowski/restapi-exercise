package com.example.restapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends User{

    private String name;

    private String surname;

    private int age;

    private String pesel;


    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();
}

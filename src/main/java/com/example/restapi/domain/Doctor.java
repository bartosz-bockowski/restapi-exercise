package com.example.restapi.domain;

import com.example.restapi.model.DoctorSpecializationType;
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
public class Doctor extends User {

    private String name;

    private String surname;

    private DoctorSpecializationType specialization;

    private int age;

    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();
}

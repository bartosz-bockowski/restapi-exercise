package com.example.restapi.domain;

import com.example.restapi.model.PatientHealthStatus;
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
@DiscriminatorValue("Patient")
public class Patient extends User{

    @Enumerated(EnumType.STRING)
    private PatientHealthStatus health;

    private String name;

    private String surname;

    private int age;

    private String pesel;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<Appointment> appointments = new ArrayList<>();

}

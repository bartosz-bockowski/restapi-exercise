package com.example.restapi.domain;

import com.example.restapi.model.PatientHealthStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Patient")
public class Patient extends User {

    @Enumerated(EnumType.STRING)
    private PatientHealthStatus health;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

}

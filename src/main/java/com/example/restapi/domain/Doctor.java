package com.example.restapi.domain;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter  //czemu nie uzywamy data w encji
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private DoctorSpecializationType specialization;
    private int age;
    @OneToMany
    private List<Appointment> appointments;
}

package com.example.restapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter  //czemu nie uzywamy data w encji
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private int age;

    private String pesel;

    //    cascade oraz rodzaje fetch, mozesz rowniez zerknac na orphanRemoval
    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();
}

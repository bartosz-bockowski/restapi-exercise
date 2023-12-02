package com.example.restapi.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {

    private String name;

    private String surname;

    private int age;
}

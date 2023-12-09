package com.example.restapi.domain;

import com.example.restapi.model.PatientHealthStatus;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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

}

package com.example.restapi.domain;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Doctor")
public class Doctor extends User {

    private DoctorSpecializationType specialization;

    @OneToMany
    private List<Appointment> appointments;

}

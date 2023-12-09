package com.example.restapi.domain;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends User {

    private DoctorSpecializationType specialization;

}

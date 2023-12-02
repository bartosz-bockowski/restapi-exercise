package com.example.restapi.model;

import lombok.Getter;

@Getter
public enum PatientHealthStatus {
    SICK(0),
    HEALTHY(1);
    private int id;
    PatientHealthStatus(int id){

    }
}

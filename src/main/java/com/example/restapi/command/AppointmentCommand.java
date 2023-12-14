package com.example.restapi.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCommand {

    @NotNull
    private LocalDate date;

    @NotNull
    private Long doctorId;
    
}

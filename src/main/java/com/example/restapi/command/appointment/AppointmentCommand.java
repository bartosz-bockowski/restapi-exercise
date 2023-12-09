package com.example.restapi.command.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCommand {

    @NotBlank(message = "sdssds")
    private LocalDate date;

    @NotBlank
    private Long doctorId;
}

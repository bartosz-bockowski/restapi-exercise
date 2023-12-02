package com.example.restapi.command;

import com.example.restapi.model.DoctorSpecializationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCommand extends UserCommand {

    private DoctorSpecializationType specialization;

}

package com.example.restapi.dto;

import com.example.restapi.model.AdminActionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminActionDTO {

    private LocalDateTime createdDate;

    private AdminActionType type;

    private AdminDTO adminDTO;

}

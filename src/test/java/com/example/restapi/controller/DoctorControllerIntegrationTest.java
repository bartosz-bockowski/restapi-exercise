package com.example.restapi.controller;

import com.example.restapi.command.DoctorCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Doctor;
import com.example.restapi.exception.DoctorNotFoundException;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
class DoctorControllerIntegrationTest {

    //symulacja calej aplikacji
    private MockMvc mockMvc;

    @Autowired
    //konfiguracja
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        doctorService.save(createNextDoctor("doctorUsername1", "doctorPese1"));
        doctorService.save(createNextDoctor("doctorUsername2", "doctorPese2"));

        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setPassword("123");
        admin.setAge(1);
        admin.setName("name");
        admin.setSurname("surname");
        admin.setPesel("adminXadmin");
        adminService.save(admin);

    }

    public Doctor createNextDoctor(String username, String pesel) {
        Doctor doctor = new Doctor();
        doctor.setUsername(username);
        doctor.setPassword("123");
        doctor.setName("name");
        doctor.setSurname("surname");
        doctor.setAge(1);
        doctor.setPesel(pesel);
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        return doctor;
    }

    @Test
    void shouldGetAllDoctors() throws Exception {
        this.mockMvc.perform(get("/api/v1/doctor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldPostDoctor() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand();
        doctorCommand.setUsername("doctorUsername3");
        doctorCommand.setPassword("pass");
        doctorCommand.setPesel("doctorPese3");
        doctorCommand.setName("nnn");
        doctorCommand.setSurname("sss");
        doctorCommand.setAge(1);
        doctorCommand.setSpecialization(DoctorSpecializationType.SPEC1);

        this.mockMvc.perform(post("/api/v1/doctor")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel", equalTo(doctorCommand.getPesel())));
    }

    @Test
    void shouldGetDoctor() throws Exception {
        this.mockMvc.perform(get("/api/v1/doctor/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel", equalTo("10000000000")));
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeleteDoctorAsDoctor() throws Exception {
        this.mockMvc.perform(delete("/api/v1/doctor/1"))
                .andExpect(status().isOk());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.findById(1L));
    }

    @Test
    @WithUserDetails(value = "admin1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeleteDoctorAsAdmin() throws Exception {
        //TODO casting error
        this.mockMvc.perform(delete("/api/v1/doctor/1"))
                .andExpect(status().isOk());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.findById(1L));
    }

}

package com.example.restapi.controller;

import com.example.restapi.command.PatientCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Patient;
import com.example.restapi.exception.PatientNotFoundException;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.PatientService;
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
public class PatientControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    long nextPesel = 10000000000L;
    long nextUsername = 100000L;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        patientService.save(createNextPatient());
        patientService.save(createNextPatient());
        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setPassword("123");
        admin.setAge(1);
        admin.setName("name");
        admin.setSurname("surname");
        admin.setPesel("adminXadmin");
        adminService.save(admin);
    }

    String getNextPesel() {
        return String.valueOf(nextPesel++);
    }

    String getNextUsername() {
        return String.valueOf(nextUsername++);
    }

    public Patient createNextPatient() {
        Patient patient = new Patient();
        patient.setUsername(getNextUsername());
        patient.setPassword("123");
        patient.setName("name");
        patient.setSurname("surname");
        patient.setAge(1);
        patient.setPesel(getNextPesel());
        return patient;
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetAllPatients() throws Exception {
        this.mockMvc.perform(get("/api/v1/patient/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldPostPatient() throws Exception {
        PatientCommand patientCommand = new PatientCommand();
        patientCommand.setUsername(getNextUsername());
        patientCommand.setPassword("pass");
        patientCommand.setPesel(getNextPesel());
        patientCommand.setName("nnn");
        patientCommand.setSurname("sss");
        patientCommand.setAge(1);

        this.mockMvc.perform(post("/api/v1/patient")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel", equalTo(patientCommand.getPesel())));
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetDoctor() throws Exception {
        this.mockMvc.perform(get("/api/v1/patient/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel", equalTo("10000000000")));
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeletePatientAsDoctor() throws Exception {
        this.mockMvc.perform(delete("/api/v1/patient/1"))
                .andExpect(status().isOk());

        Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.findById(1L));
    }

    @Test
    @WithUserDetails(value = "admin1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeletePatientAsAdmin() throws Exception {
        this.mockMvc.perform(delete("/api/v1/patient/1"))
                .andExpect(status().isOk());

        Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.findById(1L));
    }

}

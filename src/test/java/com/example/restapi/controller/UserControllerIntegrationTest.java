package com.example.restapi.controller;

import com.example.restapi.domain.Doctor;
import com.example.restapi.model.AuthenticationRequest;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        Doctor doctor = new Doctor();
        doctor.setUsername("doctor1");
        doctor.setPassword("123");
        doctor.setName("doctorName");
        doctor.setSurname("doctorSurname");
        doctor.setAge(30);
        doctor.setPesel("doctor12345");
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        doctorService.save(doctor);

    }

    @Test
    void shouldGetJwtTokenOfUser() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("doctor1");
        authenticationRequest.setPassword("123");
        this.mockMvc.perform(get("/api/v1/user/jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusForbidden() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("doctor1");
        authenticationRequest.setPassword("12344");
        this.mockMvc.perform(get("/api/v1/user/jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnStatusNotFound() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("doctor13");
        authenticationRequest.setPassword("123");
        this.mockMvc.perform(get("/api/v1/user/jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isNotFound());
    }
}

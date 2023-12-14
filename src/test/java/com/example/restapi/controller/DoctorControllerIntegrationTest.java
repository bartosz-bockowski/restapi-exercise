package com.example.restapi.controller;

import com.example.restapi.domain.Doctor;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.security.user.UserService;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.DoctorService;
import com.google.gson.Gson;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
class DoctorControllerIntegrationTest {

    @Autowired
    private UserService userService;

    //    co to - symulacja calej aplikacji
    private MockMvc mockMvc;

    @Autowired
//    co to - konfiguracja
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    long nextPesel = 10000000000L;
    long nextUsername = 100000L;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        doctorService.save(createNextDoctor());
        doctorService.save(createNextDoctor());
    }

    String getNextPesel() {
        return String.valueOf(nextPesel++);
    }

    String getNextUsername() {
        return String.valueOf(nextUsername++);
    }

    public Doctor createNextDoctor() {
        Doctor doctor = new Doctor();
        doctor.setUsername(getNextUsername());
        doctor.setPassword("123");
        doctor.setName("name");
        doctor.setSurname("surname");
        doctor.setAge(1);
        doctor.setPesel(getNextPesel());
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        return doctor;
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetAllDoctors() throws Exception {
        this.mockMvc.perform(get("/api/v1/doctor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithUserDetails(value = "100000", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldPostDoctor() throws Exception {
        this.mockMvc.perform(post("/api/v1/doctor")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(createNextDoctor())))
                .andExpect(status().isCreated());
    }

}

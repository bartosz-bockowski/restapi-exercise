package com.example.restapi.controller;

import com.example.restapi.domain.Doctor;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.service.AdminService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@ActiveProfiles("application-tests")
@AutoConfigureTestDatabase
//@ConfigurationProperties(prefix = "spring.liquibase", ignoreUnknownFields = false)
class DoctorControllerIntegrationTest {

    //    co to
    private MockMvc mockMvc;

    @Autowired
//    co to
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AdminService adminService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        doctor = createSampleDoctor();
    }

    public Doctor createSampleDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUsername("username");
        doctor.setPassword("123123123");
        doctor.setName("name");
        doctor.setSurname("surname");
        doctor.setAge(1);
        doctor.setPesel("12312312312");
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        return doctor;
    }

    @Test
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    void shouldGetAllDoctors() throws Exception {
        this.mockMvc.perform(get("/api/v1/doctor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

}

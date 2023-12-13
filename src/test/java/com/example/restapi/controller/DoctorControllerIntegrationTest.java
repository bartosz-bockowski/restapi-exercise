package com.example.restapi.controller;

import com.example.restapi.domain.Doctor;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.model.UserStatus;
import com.example.restapi.security.user.UserService;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.DoctorService;
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

    private Doctor doctor;

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        doctor = doctorService.save(createSampleDoctor());
    }

    public Doctor createSampleDoctor() {
        Doctor doctor = new Doctor();
        doctor.setUsername("username");
        doctor.setPassword("123123123");
        doctor.setName("name");
        doctor.setSurname("surname");
        doctor.setAge(1);
        doctor.setPesel("k1mvkao0611");
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        doctor.setStatus(UserStatus.ENABLED);
        doctor.setLocked(false);
        return doctor;
    }

    @Test
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetAllDoctors(@Autowired UserService userService) throws Exception {
        this.mockMvc.perform(get("/api/v1/doctor/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

}

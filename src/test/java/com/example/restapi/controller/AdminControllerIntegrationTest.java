package com.example.restapi.controller;

import com.example.restapi.command.AdminCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Doctor;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.model.UserStatus;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
public class AdminControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("123123");
        admin.setAge(3);
        admin.setName("adminek");
        admin.setSurname("adminekSur");
        admin.setPesel("admin0pesel");
        adminService.save(admin);

        Doctor doctor = new Doctor();
        doctor.setUsername("doctor1");
        doctor.setPassword("123");
        doctor.setAge(10);
        doctor.setName("doctorek");
        doctor.setSurname("doctorekSur");
        doctor.setPesel("doctorXXXXX");
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        doctorService.save(doctor);

    }

    @Test
    void shouldPostAdmin() throws Exception {
        AdminCommand adminCommand = new AdminCommand();
        adminCommand.setUsername("admin1");
        adminCommand.setPassword("123123");
        adminCommand.setAge(3);
        adminCommand.setName("adminek");
        adminCommand.setSurname("adminekSur");
        adminCommand.setPesel("adminXpesel");
        this.mockMvc.perform(post("/api/v1/admin")
                        .content(objectMapper.writeValueAsString(adminCommand))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel", equalTo("adminXpesel")));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetActionsOfAdmin() throws Exception {
        this.mockMvc.perform(get("/api/v1/admin/actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldChangeUserStatus() throws Exception {
        //TODO casting error
        this.mockMvc.perform(post("/api/v1/admin/changeStatus/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus", equalTo(UserStatus.LOCKED)));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldSwitchLocked() throws Exception {
        //TODO casting error
        this.mockMvc.perform(post("/api/v1/admin/switchLocked/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locked", equalTo(true)));
    }
}
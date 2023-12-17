package com.example.restapi.controller;

import com.example.restapi.command.PatientCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Appointment;
import com.example.restapi.domain.Doctor;
import com.example.restapi.domain.Patient;
import com.example.restapi.exception.PatientNotFoundException;
import com.example.restapi.model.AppointmentStatus;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.security.user.UserService;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.AppointmentService;
import com.example.restapi.service.DoctorService;
import com.example.restapi.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        patientService.save(createNextPatient("patient1", "patientPes1"));
        patientService.save(createNextPatient("patient2", "patientPes2"));

        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setPassword("123");
        admin.setAge(1);
        admin.setName("name");
        admin.setSurname("surname");
        admin.setPesel("adminXadmin");
        adminService.save(admin);

        Doctor doctor = new Doctor();
        doctor.setUsername("doctor1");
        doctor.setPassword("123");
        doctor.setAge(20);
        doctor.setPesel("12312332121");
        doctor.setName("doctorName");
        doctor.setSurname("doctorSurname");
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        doctorService.save(doctor);

    }

    public Patient createNextPatient(String username, String pesel) {
        Patient patient = new Patient();
        patient.setUsername(username);
        patient.setPassword("123");
        patient.setName("name");
        patient.setSurname("surname");
        patient.setAge(1);
        patient.setPesel(pesel);
        return patient;
    }

    @Test
    @Order(0)
    void shouldGetAllPatients() throws Exception {
        this.mockMvc.perform(get("/api/v1/patient/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(1)
    void shouldPostPatient() throws Exception {
        PatientCommand patientCommand = new PatientCommand();
        patientCommand.setUsername("patientUsername3");
        patientCommand.setPassword("pass");
        patientCommand.setPesel("patientPes3");
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
    @Order(2)
    void shouldGetPatient() throws Exception {
        this.mockMvc.perform(get("/api/v1/patient/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel", equalTo("patientPes1")));
    }

    @Test
    @Order(3)
    @WithUserDetails(value = "patient1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetAppointmentsOfPatient() throws Exception {
        Patient loggedPatient = (Patient) userService.getLoggedUser();
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.AWAITING);
        appointment.setDoctor(doctorService.findById(4L));
        appointment.setPatient(loggedPatient);
        appointment.setDate(LocalDate.now().minusDays(2));
        appointmentService.hardSave(appointment);

        this.mockMvc.perform(get("/api/v1/patient/myAppointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    @Order(4)
    @WithUserDetails(value = "patient2", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeletePatientAsPatient() throws Exception {
        this.mockMvc.perform(delete("/api/v1/patient/2"))
                .andExpect(status().isOk());

        Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.findById(2L));
    }

    @Test
    @Order(5)
    @WithUserDetails(value = "admin1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeletePatientAsAdmin() throws Exception {
        this.mockMvc.perform(delete("/api/v1/patient/1"))
                .andExpect(status().isOk());

        Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.findById(1L));
    }

}

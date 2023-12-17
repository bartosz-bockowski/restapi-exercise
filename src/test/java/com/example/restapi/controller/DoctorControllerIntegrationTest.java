package com.example.restapi.controller;

import com.example.restapi.command.DoctorCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Appointment;
import com.example.restapi.domain.Doctor;
import com.example.restapi.domain.Patient;
import com.example.restapi.exception.DoctorNotFoundException;
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
class DoctorControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        doctorService.save(createNextDoctor("doctor1", "doctorPese1"));
        doctorService.save(createNextDoctor("doctor2", "doctorPese2"));
        doctorService.save(createNextDoctor("doctorToDelete1", "doctorPese3"));
        doctorService.save(createNextDoctor("doctorToDelete2", "doctorPese4"));

        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setPassword("123");
        admin.setAge(1);
        admin.setName("name");
        admin.setSurname("surname");
        admin.setPesel("adminXadmin");
        adminService.save(admin);

        Patient patient = new Patient();
        patient.setUsername("patient1");
        patient.setPassword("123");
        patient.setAge(14);
        patient.setName("patientName");
        patient.setSurname("patientSurname");
        patient.setPesel("patientPes1");
        patientService.save(patient);

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
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void shouldPostDoctor() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand();
        doctorCommand.setUsername("doctorUsername3");
        doctorCommand.setPassword("pass");
        doctorCommand.setPesel("doctorPese5");
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
                .andExpect(jsonPath("$.pesel", equalTo("doctorPese1")));
    }

    @Test
    @WithUserDetails(value = "doctorToDelete2", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeleteDoctorAsDoctor() throws Exception {
        this.mockMvc.perform(delete("/api/v1/doctor/4"))
                .andExpect(status().isOk());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.findById(4L));
    }

    @Test
    @WithUserDetails(value = "admin1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldDeleteDoctorAsAdmin() throws Exception {
        this.mockMvc.perform(delete("/api/v1/doctor/3"))
                .andExpect(status().isOk());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.findById(3L));
    }

    @Test
    @WithUserDetails(value = "doctor1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldGetAppointmentsOfDoctor() throws Exception {
        Doctor loggedDoctor = (Doctor) userService.getLoggedUser();
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.AWAITING);
        appointment.setDoctor(loggedDoctor);
        appointment.setPatient(patientService.findById(6L));
        appointment.setDate(LocalDate.now().minusDays(2));
        appointmentService.hardSave(appointment);

        this.mockMvc.perform(get("/api/v1/doctor/myAppointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

}

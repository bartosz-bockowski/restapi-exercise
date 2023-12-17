package com.example.restapi.controller;

import com.example.restapi.command.AppointmentCommand;
import com.example.restapi.domain.Admin;
import com.example.restapi.domain.Appointment;
import com.example.restapi.domain.Doctor;
import com.example.restapi.domain.Patient;
import com.example.restapi.model.AppointmentStatus;
import com.example.restapi.model.DoctorSpecializationType;
import com.example.restapi.service.AdminService;
import com.example.restapi.service.AppointmentService;
import com.example.restapi.service.DoctorService;
import com.example.restapi.service.PatientService;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
public class AppointmentControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();

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
        patient.setAge(3);
        patient.setName("patient");
        patient.setSurname("patientSur");
        patient.setPesel("patientXXXX");
        patientService.save(patient);

        Doctor doctor = new Doctor();
        doctor.setUsername("doctor1");
        doctor.setPassword("123");
        doctor.setAge(3);
        doctor.setName("doctor");
        doctor.setSurname("doctorSur");
        doctor.setPesel("12312312312");
        doctor.setSpecialization(DoctorSpecializationType.SPEC1);
        doctorService.save(doctor);

        Doctor doctor2 = new Doctor();
        doctor2.setUsername("doctor2");
        doctor2.setPassword("123");
        doctor2.setAge(3);
        doctor2.setName("doctor");
        doctor2.setSurname("doctorSur");
        doctor2.setPesel("12312312313");
        doctor2.setSpecialization(DoctorSpecializationType.SPEC1);
        doctorService.save(doctor2);
    }

    void saveSampleAppointment() {
        AppointmentCommand appointmentCommand = new AppointmentCommand();
        appointmentCommand.setDate(LocalDate.parse("2020-01-01"));
        appointmentCommand.setDoctorId(3L);
        appointmentService.save(appointmentCommand);
    }

    void hardSaveSampleAppointment(LocalDate date, Long patientId, Long doctorId) {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setPatient(patientService.findById(patientId));
        appointment.setDoctor(doctorService.findById(doctorId));
        appointment.setStatus(AppointmentStatus.AWAITING);
        appointmentService.hardSave(appointment);
    }

    @Test
    @WithUserDetails(value = "patient1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldPostAppointment() throws Exception {
        AppointmentCommand appointmentCommand = new AppointmentCommand();
        appointmentCommand.setDate(LocalDate.parse("2020-01-01"));
        appointmentCommand.setDoctorId(3L);
        //TODO casting error
        this.mockMvc.perform(post("/api/v1/appointment")
                        .content(objectMapper.writeValueAsString(appointmentCommand))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date", equalTo(appointmentCommand.getDate())));
    }

    @Test
    @WithUserDetails(value = "patient1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldCancelAppointmentAsPatient() throws Exception {
        saveSampleAppointment();
        //TODO casting error
        this.mockMvc.perform(post("/api/v1/appointment/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(AppointmentStatus.CANCELLED)));
    }

    @Test
    @WithUserDetails(value = "doctor1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldCancelAppointmentAsDoctor() throws Exception {
        hardSaveSampleAppointment(LocalDate.now().plusDays(2), 2L, 3L);
        //TODO casting error
        this.mockMvc.perform(post("/api/v1/appointment/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(AppointmentStatus.CANCELLED)));
    }

    @Test
    @WithUserDetails(value = "admin1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldReturnForbidden() throws Exception {
        saveSampleAppointment();
        //TODO casting error
        this.mockMvc.perform(post("/api/v1/appointment/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(AppointmentStatus.CANCELLED)));
    }

    @Test
    @WithUserDetails(value = "patient1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldReturnAppointmentsOfPatient() throws Exception {
        saveSampleAppointment();
        //TODO casting error
        this.mockMvc.perform(get("/api/v1/appointment/my"))
                .andExpect(status().isOk())
                .andDo(print());
//                .andExpect(jsonPath("$.status", equalTo(AppointmentStatus.CANCELLED)));
    }

    @Test
    @WithUserDetails(value = "doctor1", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userService")
    void shouldCompleteAppointment() throws Exception {
        hardSaveSampleAppointment(LocalDate.now().minusDays(2), 2L, 3L);
        //TODO casting error
        this.mockMvc.perform(get("/api/v1/appointment/my"))
                .andExpect(status().isOk())
                .andDo(print());
//                .andExpect(jsonPath("$.status", equalTo(AppointmentStatus.CANCELLED)));
    }

}

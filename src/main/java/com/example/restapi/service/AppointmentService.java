package com.example.restapi.service;

import com.example.restapi.command.AppointmentCommand;
import com.example.restapi.domain.Appointment;
import com.example.restapi.domain.Doctor;
import com.example.restapi.domain.Patient;
import com.example.restapi.domain.User;
import com.example.restapi.dto.DoctorAppointmentDTO;
import com.example.restapi.dto.PatientAppointmentDTO;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.exception.AppointmentCompletionTooEarlyException;
import com.example.restapi.exception.AppointmentNotFoundException;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.model.AppointmentStatus;
import com.example.restapi.model.PatientHealthStatus;
import com.example.restapi.repository.AppointmentRepository;
import com.example.restapi.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final ModelMapper modelMapper;

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final UserService userService;

    public Appointment save(AppointmentCommand appointmentCommand) {
        userService.getLoggedUser().checkEnabled();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Appointment appointment = modelMapper.map(appointmentCommand, Appointment.class);

        Doctor doctor = doctorService.findById(appointmentCommand.getDoctorId());
        appointment.setDoctor(doctor);

        appointment.setPatient((Patient) userService.getLoggedUser());

        appointment.setStatus(AppointmentStatus.AWAITING);

        appointmentRepository.save(appointment);

        return appointment;
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.getAppointmentsByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.getAppointmentsByDoctorId(doctorId);
    }

    public Class getDTOforLoggedUser() {
        String userType = userService.getLoggedUser().getUserType();
        return switch (userType) {
            case "Patient" -> PatientAppointmentDTO.class;
            case "Doctor" -> DoctorAppointmentDTO.class;
            default -> null;
        };
    }

    public Appointment cancelById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment doesn't exist!"));
        if (!List.of(appointment.getDoctor().getId(), appointment.getPatient().getId()).contains(userService.getLoggedUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return appointment;
    }

    public Appointment completeById(Long appointmentId, int healthId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment doesn't exist!"));

        userService.getLoggedUser().checkEnabled();
        if (!Objects.equals(appointment.getDoctor().getId(), userService.getLoggedUser().getId())) {
            throw new AccessDeniedException("Access denied!");
        }
        if (appointment.getDate().isAfter(LocalDate.now())) {
            throw new AppointmentCompletionTooEarlyException("It's too early to complete this appointment!");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Patient patient = appointment.getPatient();
        for (PatientHealthStatus status : PatientHealthStatus.values()) {
            //if id is wrong, then no change
            if (status.getId() == healthId) {
                patient.setHealth(status);
                break;
            }
        }
        patientService.update(patient);
        return appointmentRepository.save(appointment);
    }

    public List<?> getAppointmentsOfLoggedUser() {
        User user = userService.getLoggedUser();
        Long userId = user.getId();
        return switch (user.getUserType()) {
            case "Patient" -> getAppointmentsByPatientId(userId).stream()
                    .map(appointment -> modelMapper.map(appointment, PatientAppointmentDTO.class)).toList();
            case "Doctor" -> getAppointmentsByDoctorId(userId).stream()
                    .map(appointment -> modelMapper.map(appointment, DoctorAppointmentDTO.class)).toList();
            default -> throw new UserNotFoundException("You are not applicable for appointments!");
        };
    }
}

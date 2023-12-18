package com.example.restapi.domain;

import com.example.restapi.model.AppointmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @CreatedDate
    private LocalDateTime createdTime;

    @ManyToOne
    @ToString.Exclude
    private Patient patient;

    @ManyToOne
    @ToString.Exclude
    private Doctor doctor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @LastModifiedBy
    @ManyToOne
    @ToString.Exclude
    private User lastChangedBy;

    @Version
    private int version;
}

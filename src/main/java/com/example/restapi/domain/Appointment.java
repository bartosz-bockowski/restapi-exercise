package com.example.restapi.domain;

import com.example.restapi.model.AppointmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter  //czemu nie uzywamy data w encji
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    //@TODO
    //wczytywanie rol z jwt - ZROBIONE
    //patient tworzy appointmenty - ZROBIONE
    //doctor i patient wyswietlanie swoich appointmentow - ZROBIONE
    //doctor i patient anulowanie appointmentow
    //status appointmentu - enum - doktor ustawia - ZROBIONE
    //dotkor zrealizowanie appointmentu - tylko po dacie zaplanowanej - ZROBIONE
    //doktor przy realizacji podaje stan zdrowia pacjenta - enum - ZROBIONE
    //obiekt admin - ZROBIONE
    //obiekt admin usuwa wszystkie konta - ZROBIONE
    //doktor i pacjent usuwaja tylko swoje konta - ZROBIONE
    //akcje admina tworza obiekt action - id - admin - enum co sie stalo - ZROBIONE
    //atrybut admin z @createdby
    //atrybut @createdby appointment nad patient

    //auditor provider oraz auditoraware
//    @EntityListener
//    AuditinEntityListener do nasluchiwania tzn
//    Aby poprawnie samo ustawiało sie pole oznaczone adntoacja @CreatedBy
//    musi to byc skonfigurowane
//    Pocyztaj rowniez o polu
//    @LastModifiedBy
//    @CreateDate
//    @LastModifiedDate
//    @Version

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @CreatedBy
    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}

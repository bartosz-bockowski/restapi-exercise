package com.example.restapi.domain;

import com.example.restapi.model.AppointmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter  //czemu nie uzywamy data w encji
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
public class Appointment {

    //@TODO

//    Dodaj cancellowanie przez pacjenta, - ZROBIONE
//    Dodaj w appointment version, lastModifiedBy, createdBy createdDate i createdBy - PROBLEMY
//    Obiekt action bedzie mial nowe pole createdDate - ZROBIONE
//    ZROBIONE - Dodaj endpoint dla admina do usuwania pacjentow i doktorow i nakladania na nich ograniczen - zmienianie statusu konta locked i enabled,
//    ZROBIONE - ma to potem zostac rowniez wdrozone jako walidacja przy np zamawianiu wizyty przez pacjenta. To znaczy ze jesli pacjent bedzie mial konto locked to nie moze sie umowic na wizyte u danego doktora etc..
//    Teraz tylko admin bedzie mogl edytowac i usuwac konta po ID - ZROBIONE
//    Zrób endpoint który umożliwi adminowi wyświetlenie listy swoich akcji - ZROBIONE

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
//    @CreatedDate
//    private LocalDateTime createdTime;
//    @CreatedBy
    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
//    @LastModifiedBy
//    @ManyToOne
//    private User lastChangedBy;
//    @Version
//    private int version;
}

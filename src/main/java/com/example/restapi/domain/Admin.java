package com.example.restapi.domain;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Admin")
public class Admin extends User {

    @OneToMany(mappedBy = "admin")
    private List<AdminAction> actions = new ArrayList<>();

}

package com.example.restapi.domain;

import com.example.restapi.exception.UserLockedException;
import com.example.restapi.model.UserStatus;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "userType", discriminatorType = DiscriminatorType.STRING)
@Table(name = "userCustomName")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Length(min = 6)
    @NotNull
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Min(1)
    private int age;

    @Length(min = 11, max = 11)
    private String pesel;

    private boolean locked = false;

    @Column(updatable = false, insertable = false)
    private String userType;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ENABLED;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.userType));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ENABLED;
    }

    public void checkEnabled() {
        if (this.status == UserStatus.LOCKED) {
            throw new UserLockedException("You are locked as a user!");
        }
    }
}

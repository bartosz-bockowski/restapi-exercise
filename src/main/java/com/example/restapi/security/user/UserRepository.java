package com.example.restapi.security.user;

import com.example.restapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPesel(String pesel);

    boolean existsByPeselAndUsername(String pesel, String username);
}

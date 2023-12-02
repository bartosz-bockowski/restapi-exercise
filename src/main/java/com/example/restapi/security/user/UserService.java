package com.example.restapi.security.user;

import com.example.restapi.domain.User;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.repository.DoctorRepository;
import com.example.restapi.repository.PatientRepository;
import com.example.restapi.security.role.Role;
import com.example.restapi.security.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    public User getLoggedUser() {
        User user = findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        if(Objects.isNull(user)){
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).isPresent() ?
                userRepository.findByUsername(username).get() : null;
    }

    public void setRolesByUserName(String username, List<Role> roles){
        User user = findByUserName(username);
        user.setRoles(new HashSet<>(roles));
    }

    public User saveUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User saveUserRaw(User user){
        return userRepository.save(user);
    }

    public boolean checkAdmin(){
        return getLoggedUser().getUserType().equals("Admin");
    }

    public void deleteCheck(Long id){
        if(!id.equals(getLoggedUser().getId()) && !checkAdmin()){
            throw new AccessDeniedException("Access denied!");
        }
    }

}
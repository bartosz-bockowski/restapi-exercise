package com.example.restapi.security.user;

import com.example.restapi.domain.User;
import com.example.restapi.dto.UserDTO;
import com.example.restapi.exception.AccessDeniedException;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.model.AdminActionType;
import com.example.restapi.model.UserStatus;
import com.example.restapi.repository.DoctorRepository;
import com.example.restapi.repository.PatientRepository;
import com.example.restapi.security.jwt.JwtService;
import com.example.restapi.security.role.Role;
import com.example.restapi.security.role.RoleRepository;
import com.example.restapi.service.AdminActionService;
import lombok.RequiredArgsConstructor;
import org.aspectj.asm.IModelFilter;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public User getLoggedUser() {
        User user = findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).isPresent() ?
                userRepository.findByUsername(username).get() : null;
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void setRolesByUserName(String username, List<Role> roles) {
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

    public boolean checkAdmin() {
        return Objects.equals(getLoggedUser().getUserType(), "Admin");
    }

    public void deleteCheck(Long id) {
        if (!Objects.equals(getLoggedUser().getId(), id) && !checkAdmin()) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public String generateTokenFromUserInput(User userInput){
        User user = findByUserName(userInput.getUsername());
        if(Objects.isNull(user) || !BCrypt.checkpw(userInput.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Bad credentials!");
        }
        return jwtService.generateToken(user);
    }

    public boolean isAdmin(){
        return Objects.equals(getLoggedUser().getUserType(),"Admin");
    }

}
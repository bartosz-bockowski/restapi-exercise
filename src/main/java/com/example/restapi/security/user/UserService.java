package com.example.restapi.security.user;

import com.example.restapi.domain.User;
import com.example.restapi.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User getLoggedUser() {
        User user = findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteCheck(Long id) {
        if (!Objects.equals(getLoggedUser().getId(), id)) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public void checkIfUserExistsByPeselAndUsername(String pesel, String username) {
        if (userRepository.existsByPeselAndUsername(pesel, username)) {
            throw new UserWithThisUsernameAndPeselAlreadyExistsException("Username and pesel are taken");
        }
        if (userRepository.existsByUsername(username)) {
            throw new UserWithThisUsernameAlreadyExistsException("Username is taken");
        }
        if (userRepository.existsByPesel(pesel)) {
            throw new UserWithThisPeselAlreadyExistsException("Pesel is taken");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = Optional.ofNullable(findByUserName(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getUserType().toUpperCase()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }

}
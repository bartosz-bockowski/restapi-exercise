package com.example.restapi.security.config;

import com.example.restapi.domain.User;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceTest {

    private final UserRepository userRepository;

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}

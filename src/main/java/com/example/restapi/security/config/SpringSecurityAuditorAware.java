package com.example.restapi.security.config;

import com.example.restapi.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<User> {

    private final UserServiceTest userServiceTest;

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of(userServiceTest.findByUserName(SecurityContextHolder
                .getContext().getAuthentication().getName()));
    }
}

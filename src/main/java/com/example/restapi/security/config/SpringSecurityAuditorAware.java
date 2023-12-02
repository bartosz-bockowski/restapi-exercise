package com.example.restapi.security.config;

import com.example.restapi.domain.User;
import com.example.restapi.security.user.SpringDataUserDetailsService;
import com.example.restapi.security.user.UserService;
import liquibase.pro.packaged.U;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {

    private final UserService userService;

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of(userService.findByUserName(SecurityContextHolder.getContext().getAuthentication()
                                                                                                .getName()));
    }
}

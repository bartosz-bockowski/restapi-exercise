package com.example.restapi.config;

import com.example.restapi.security.config.SpringSecurityAuditorAware;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Set;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class Config {

    @Bean
    public ModelMapper modelMapper(Set<Converter> converterSet) {
        ModelMapper modelMapper = new ModelMapper();
        converterSet.forEach(modelMapper::addConverter);

        return modelMapper;
    }

    @Bean
    public SpringSecurityAuditorAware auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

}

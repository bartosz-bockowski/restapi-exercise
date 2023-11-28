package com.example.restapi.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ModelMapper modelMapper(Set<Converter> converterSet) {
        ModelMapper modelMapper = new ModelMapper();
        converterSet.forEach(modelMapper::addConverter);

        return modelMapper;
    }

}

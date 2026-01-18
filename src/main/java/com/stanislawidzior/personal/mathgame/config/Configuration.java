package com.stanislawidzior.personal.mathgame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

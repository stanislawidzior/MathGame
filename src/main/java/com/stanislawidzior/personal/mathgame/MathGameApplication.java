package com.stanislawidzior.personal.mathgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.stanislawidzior.personal.mathgame.config")
public class MathGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(MathGameApplication.class, args);
    }

}

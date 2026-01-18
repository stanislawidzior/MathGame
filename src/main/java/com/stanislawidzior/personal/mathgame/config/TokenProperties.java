package com.stanislawidzior.personal.mathgame.config;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

    @Validated
    @ConfigurationProperties("token")
    public record TokenProperties(
            @NotBlank(message = "Signing key cannot be blank")
            @Name("signing.key")
            String signingKey,

            @DurationMin(seconds = 3600, message = "Expiration time must be more than or equal to 3600 seconds (1 hour)")
            @DurationMax(seconds = 86400, message = "Expiration time must be less than or equal to 86400 seconds (1 day)")
            @DurationUnit(ChronoUnit.SECONDS)
            @Name("expiration.time.seconds")
            Duration expirationTimeSeconds
    ) { }


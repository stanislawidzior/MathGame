package com.stanislawidzior.personal.mathgame.security.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Username cannot be null")
    @Size(min=3, max = 20, message = "Username must be between 3 and 20 characters long.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be valid email address.")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;

    @NotNull(message = "Confirm password cannot be null")
    private String confirmPassword;
}

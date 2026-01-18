package com.stanislawidzior.personal.mathgame.security.controller;

import com.stanislawidzior.personal.mathgame.data.entity.AppUser;
import com.stanislawidzior.personal.mathgame.security.dto.request.RegisterRequest;
import com.stanislawidzior.personal.mathgame.security.dto.request.SignInRequest;
import com.stanislawidzior.personal.mathgame.security.dto.response.GenericResponse;
import com.stanislawidzior.personal.mathgame.security.dto.response.SignInResponse;
import com.stanislawidzior.personal.mathgame.security.dto.response.UserSummaryResponse;
import com.stanislawidzior.personal.mathgame.security.service.AuthService;
import com.stanislawidzior.personal.mathgame.security.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserSummaryResponse>> registerUser(@Valid @RequestBody RegisterRequest request){
        UserSummaryResponse response = authService.register(request);
        return ResponseEntity.ok(GenericResponse.success("User registered successfully", response));
    }

    @PostMapping("/signin")
    public ResponseEntity<GenericResponse<SignInResponse>>  signInUser(@Valid @RequestBody SignInRequest request){
        SignInResponse response = authService.signIn(request);
        return ResponseEntity.ok(GenericResponse.success("User logged in successfully", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<Void>> logoutUser(@AuthenticationPrincipal AppUser user){
        authService.logout(user);
        return ResponseEntity.ok(GenericResponse.success("User logged out", null));
    }

    @GetMapping("/me")
    public ResponseEntity<GenericResponse<UserSummaryResponse>> getCurrentUser(@AuthenticationPrincipal AppUser user){
        UserSummaryResponse response = authService.me(user);
        return ResponseEntity.ok(GenericResponse.success("User details retrieved successfully", response));
    }

}

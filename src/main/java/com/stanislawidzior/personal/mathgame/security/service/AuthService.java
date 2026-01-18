package com.stanislawidzior.personal.mathgame.security.service;

import com.stanislawidzior.personal.mathgame.data.entity.AppUser;
import com.stanislawidzior.personal.mathgame.data.service.IAppUserService;
import com.stanislawidzior.personal.mathgame.security.config.util.JwtUtil;
import com.stanislawidzior.personal.mathgame.security.dto.request.RegisterRequest;
import com.stanislawidzior.personal.mathgame.security.dto.request.SignInRequest;
import com.stanislawidzior.personal.mathgame.security.dto.response.SignInResponse;
import com.stanislawidzior.personal.mathgame.security.dto.response.UserSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService implements IAuthService {
    private final IAppUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public SignInResponse signIn(@Valid @RequestBody SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        AppUser user = (AppUser) userService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(user.getUsername());
        log.info("User {} successfully signed in", user.getUsername());
        return SignInResponse.builder()
                .token(jwt)
                .userId(user.getId())
                .build();
    }

    @Override
    public UserSummaryResponse register(RegisterRequest request) {
        AppUser user = userService.createUser(request);
        return me(user);
    }

    @Override
    public UserSummaryResponse me(AppUser user) {
        return UserSummaryResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .build();
    }

    @Override
    public void logout(AppUser user) {
        log.info("User {} has logged out", user.getUsername());
    }
}

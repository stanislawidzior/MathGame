package com.stanislawidzior.personal.mathgame.data.service;


import com.stanislawidzior.personal.mathgame.data.entity.AppUser;
import com.stanislawidzior.personal.mathgame.data.entity.AppUserRole;
import com.stanislawidzior.personal.mathgame.data.repository.AppUserRepository;
import com.stanislawidzior.personal.mathgame.security.dto.request.RegisterRequest;
import com.stanislawidzior.personal.mathgame.security.exception.PasswordsDoNotMatchException;
import com.stanislawidzior.personal.mathgame.security.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Slf4j
@AllArgsConstructor
@Service
public class AppUserService implements IAppUserService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AppUser createUser(RegisterRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with given username already exists.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with given email already exists.");
        }
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        AppUser user = AppUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AppUserRole.USER)
                .build();
        AppUser savedUser = userRepository.save(user);
        log.info("User {} successfully registered", user.getUsername());
        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with given username does not exist."));
    }
}

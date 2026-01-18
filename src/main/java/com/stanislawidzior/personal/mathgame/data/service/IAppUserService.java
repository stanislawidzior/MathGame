package com.stanislawidzior.personal.mathgame.data.service;

import com.stanislawidzior.personal.mathgame.data.entity.AppUser;
import com.stanislawidzior.personal.mathgame.security.dto.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAppUserService extends UserDetailsService {
    AppUser createUser(RegisterRequest request);
}

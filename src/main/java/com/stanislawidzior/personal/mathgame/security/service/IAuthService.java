package com.stanislawidzior.personal.mathgame.security.service;

import com.stanislawidzior.personal.mathgame.data.entity.AppUser;
import com.stanislawidzior.personal.mathgame.security.dto.request.RegisterRequest;
import com.stanislawidzior.personal.mathgame.security.dto.request.SignInRequest;
import com.stanislawidzior.personal.mathgame.security.dto.response.SignInResponse;
import com.stanislawidzior.personal.mathgame.security.dto.response.UserSummaryResponse;

public interface IAuthService {
    SignInResponse signIn(SignInRequest request);
    UserSummaryResponse register(RegisterRequest request);
    UserSummaryResponse me(AppUser user);
    void logout(AppUser user);
}

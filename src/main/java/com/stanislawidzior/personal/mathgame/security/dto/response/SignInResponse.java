package com.stanislawidzior.personal.mathgame.security.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private Long userId;
    private String token;
}

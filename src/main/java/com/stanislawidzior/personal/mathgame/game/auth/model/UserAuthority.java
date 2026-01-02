package com.stanislawidzior.personal.mathgame.game.auth.model;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {
    private UserRole authority;
    public UserAuthority(UserRole authority) {
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return authority.toString();
    }
}

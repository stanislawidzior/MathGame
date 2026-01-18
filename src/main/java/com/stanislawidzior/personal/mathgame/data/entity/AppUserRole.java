package com.stanislawidzior.personal.mathgame.data.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum AppUserRole {
    USER;
    public SimpleGrantedAuthority asAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }

    @Override
    public String toString() {
        return name();
    }
}

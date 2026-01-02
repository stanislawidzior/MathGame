package com.stanislawidzior.personal.mathgame.game.model;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class GameUser {
    private String name;
    private String sessionId;
    private UUID userId;
    private boolean inGame = false;
    private String gameRoomId = null;

    public GameUser(String name) {
        this.name = name;
    }
    public GameUser(String name, String sessionId){
        this.name = name;
        this.sessionId = sessionId;
    }
}

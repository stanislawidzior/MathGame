package com.stanislawidzior.personal.mathgame.game.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@EqualsAndHashCode
@Setter
@Getter
public class GamePlayer {
    private UUID id;
    private String name;
    private int questionCounter;
    private boolean active = true;
    private String gameId;
    public GamePlayer(UUID id, String name, int questionCounter) {
        this.id = id;
        this.name = name;
        this.questionCounter = questionCounter;
    }

    public GamePlayer() {

    }


}

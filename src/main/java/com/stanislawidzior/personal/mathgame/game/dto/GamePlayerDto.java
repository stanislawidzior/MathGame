package com.stanislawidzior.personal.mathgame.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePlayerDto {
    private String name;
    public GamePlayerDto(String name) {
        this.name = name;
    }

}

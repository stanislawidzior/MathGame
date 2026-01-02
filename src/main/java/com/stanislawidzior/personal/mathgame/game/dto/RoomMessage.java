package com.stanislawidzior.personal.mathgame.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomMessage {
    private String roomId;
    private String message;

}

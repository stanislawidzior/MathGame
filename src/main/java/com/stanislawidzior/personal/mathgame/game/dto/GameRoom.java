package com.stanislawidzior.personal.mathgame.game.dto;

import com.stanislawidzior.personal.mathgame.game.model.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class GameRoom {
    private String roomId;
    private List<GamePlayerDto> players = new ArrayList<>();
    private boolean gameInProgress = false;

}

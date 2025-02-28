package com.personalprojectspjatk.mentalmathgame.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class GameRoom {
    private int roomId;
    private Set<GamePlayer> availableRoom = new HashSet<>();

}

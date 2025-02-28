package com.personalprojectspjatk.mentalmathgame.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Setter
@Getter
public class GameState {

    Map<String,Integer> players;
    public GameState(){

    }
    public GameState(Map<String, Integer> players) {
        this.players = players;
    }

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Integer> players) {
        this.players = players;
    }
}

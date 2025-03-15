package com.personalprojectspjatk.mentalmathgame.service;

import com.personalprojectspjatk.mentalmathgame.excpetions.GamePlayerNotFoundException;
import com.personalprojectspjatk.mentalmathgame.model.GamePlayer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class RandomUserService {
    private HashMap<String,GamePlayer> gamePlayerMap = new HashMap<>();



    public void setNewGamePlayerIdentifier(GamePlayer player) throws GamePlayerNotFoundException{
        var id = UUID.randomUUID().toString();
        if(gamePlayerMap.containsKey(id)){
            throw new GamePlayerNotFoundException();
        }
        gamePlayerMap.put(id, player);
        player.setId(id);
    }
    public GamePlayer getGamePlayerFromSession(String sessionId) throws GamePlayerNotFoundException{
        if(!gamePlayerMap.containsKey(sessionId)){
            throw new GamePlayerNotFoundException();
        }
        return gamePlayerMap.get(sessionId);
    }
}

package com.personalprojectspjatk.mentalmathgame.service;



import com.personalprojectspjatk.mentalmathgame.contract.*;
import com.personalprojectspjatk.mentalmathgame.excpetions.GameException;
import com.personalprojectspjatk.mentalmathgame.excpetions.GameNotFoundException;
import com.personalprojectspjatk.mentalmathgame.excpetions.NoRoomsAvailableException;
import com.personalprojectspjatk.mentalmathgame.gameSession.GameSession;
import com.personalprojectspjatk.mentalmathgame.model.GamePlayer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameSessionService {
    private List<GameSession> activeGames = new ArrayList<>();

    public List<GameRoom> getAvailableGameRooms(){
        if(activeGames.isEmpty()){
            throw new NoRoomsAvailableException();
        }
        var availableRooms = new ArrayList<GameRoom>();
        activeGames.stream().forEach(a->availableRooms.add(new GameRoom(a.getGameId(), a.getPlayers())));

        return availableRooms;
    }
    public int createNewGame(GameSettingsDto settings, GamePlayer player) {
        int gameId = activeGames.size();
        GameSession newGameSession = new GameSession(gameId);
        newGameSession.setGameSetup(new GameSetup(settings));
        newGameSession.addPlayer(player);
        activeGames.add(newGameSession);
        return gameId;
    }
    public String getGameStateForGame(int gameId) {
        GameSession gameSession = activeGames.get(gameId);
        if(gameSession.isGameStarted()){
            return "game in progress";
        }
        else{
            return "game not started";
        }
    }
   public boolean validatePlayerAnswer(int roomId, String player, int answer){
        var room = activeGames.get(roomId);
        Question question = room.getPlayerCurrentQuestion(player);
        if(question.validate(answer)) return true;
        return false;
   }

    public int joinExistingGame(int gameId, GamePlayer player) throws GameNotFoundException {
        GameSession gameSession = activeGames.get(gameId);

        if (gameSession == null) {
            throw new GameNotFoundException("Game of id " + gameId +" not found!");
        }

        gameSession.addPlayer(player);
        return gameId;
    }

    public IGameResponse getNextQuestionForPlayer(int gameId, String player){
        try{
           return activeGames.get(gameId).getNextQuestionForPlayer(player);
       }catch(GameException e){
           return new GameResponse("no more questions");
       }
    }

    public void endGame(int gameId, GamePlayer player) {
        GameSession gameSession = activeGames.get(gameId);
        if (gameSession != null) {
            gameSession.endGameForPlayer(player);
            if(gameSession.isGameOver()){
                endGame(gameId);
            }
        }
    }
    public void endGame(int gameId) {

    }

    public GameSession getGameSession(int gameId) {
        return activeGames.get(gameId);
    }

    public boolean gameExists(int gameId) {
        return activeGames.get(gameId)!= null;
    }
}


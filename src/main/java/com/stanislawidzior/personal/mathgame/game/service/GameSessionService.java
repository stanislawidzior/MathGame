package com.stanislawidzior.personal.mathgame.game.service;




import com.stanislawidzior.personal.mathgame.game.dto.*;
import com.stanislawidzior.personal.mathgame.game.exception.*;
import com.stanislawidzior.personal.mathgame.game.model.GameUser;
import com.stanislawidzior.personal.mathgame.game.model.GamePlayer;
import com.stanislawidzior.personal.mathgame.game.model.GameSettings;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {
    private ConcurrentHashMap<String,GameSession> activeGames = new ConcurrentHashMap<>();

    public List<GameRoom> getAvailableGameRooms() throws NoRoomsAvailableException {
        if(activeGames.isEmpty()){
            throw new NoRoomsAvailableException();
        }
        return activeGames.entrySet().stream()
                .map(entry ->  new GameRoom(entry.getKey(), entry.getValue().getPlayers()
                        .stream()
                        .map(player->new GamePlayerDto(player.getName())).toList(), entry.getValue().isGameStarted())).toList();


    }
    public String createNewGame(GameSettingsDto settings, GameUser user) {
        var player = createGamePlayer(user);
        var gameId = createNewGameSessionAndReturnSessionId(player, settings);
        return gameId;
    }
    public String getGameStateForGame(String gameId) {
        GameSession gameSession = activeGames.get(gameId);
        if(gameSession.isGameStarted()){
            return "game in progress";
        }
        else{
            return "game not started";
        }
    }
    public GamePlayer createGamePlayer(GameUser user){
        var player = new GamePlayer();
        player.setId(user.getUserId());
        player.setName(user.getName());
        return player;
    }
    public String createNewGameSessionAndReturnSessionId(GamePlayer player, GameSettingsDto settings){
        UUID gameId = UUID.randomUUID();
        GameSession newGameSession = new GameSession(gameId, player);
        newGameSession.setGameSetup(new GameSetup(new GameSettings(settings.getAllowedOperations(), settings.getQuestionAmount())));

        activeGames.putIfAbsent(gameId.toString(), newGameSession);
        return gameId.toString();
    }


    public String joinExistingGame(String gameId, GameUser user) throws GameNotFoundException, UserAlreadyInGameException {
        if(gameId == activeGames.keys().nextElement()){}
        GameSession gameSession = activeGames.get(gameId);

        if(user.isInGame()){
            throw new UserAlreadyInGameException();
        }
        if (gameSession == null) {
            throw new GameNotFoundException(gameId);
        }

        gameSession.addPlayer(createGamePlayer(user));
        return gameId;
    }
    public void startGame(GameUser user) throws GameNotFoundException{
        var roomId = user.getGameRoomId();
        if(roomId == null){
            throw new GameNotFoundException(roomId);
        }
        activeGames.get(roomId).startGameIfOwner(user.getUserId());

    }
    public List<UUID> getGamePlayersIdFromSession(String roomId){
        return activeGames.get(roomId).getPlayers().stream().map(GamePlayer::getId).toList();

    }
    public boolean validatePlayerAnswer(GamePlayer player, String roomId, int answer){

        var room = activeGames.get(roomId);
        Question question = room.getPlayerCurrentQuestion(player);
        if(question.validate(answer)) return true;
        return false;
    }
    public IGameResponse getNextQuestionForPlayer(int gameId, String player) throws GamePlayerNotFoundException {
        try{
           return activeGames.get(gameId).getNextQuestionForPlayer(player);
       }catch(GameException e){
           return new GameResponse("no more questions");
       }
    }

    public void endGame(String gameId) {
        GameSession gameSession = activeGames.get(gameId);
        gameSession.endGameForAllPlayers();
        if(gameSession.isGameOver()){
            activeGames.remove(gameId);
        }

    }
    public void endGamePlayer(String gameId, GamePlayer player) throws GameNotFoundException{
        var game = activeGames.get(gameId);
        if(game == null){
            throw new GameNotFoundException(gameId);
        }
        game.endGameForPlayer(player);
    }

    public GameSession getGameSession(int gameId) {
        return activeGames.get(gameId);
    }

}


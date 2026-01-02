package com.stanislawidzior.personal.mathgame.game.service;



import com.stanislawidzior.personal.mathgame.game.dto.GameState;
import com.stanislawidzior.personal.mathgame.game.dto.Question;
import com.stanislawidzior.personal.mathgame.game.exception.GameException;
import com.stanislawidzior.personal.mathgame.game.exception.GamePlayerNotFoundException;
import com.stanislawidzior.personal.mathgame.game.model.GamePlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameSession {
        private UUID gameId;
        private ConcurrentHashMap<UUID,GamePlayer> players = new ConcurrentHashMap<>();
        private GamePlayer owner;
        private GameSetup gameSetup;
        private final int MAX_SIZE = 5;
        private boolean gameStarted = false;

        public GameSession(UUID gameId, GamePlayer owner) {
            this.owner = owner;
            this.gameId = gameId;
            players.put(owner.getId(), owner);
        }
        public GameState getGameState(){
            var map = new HashMap<String,Integer>();
            players.values().stream().map(p-> map.put(p.getName(),p.getQuestionCounter()) );
            return new GameState(map);

        }
        public void startGameIfOwner(UUID userId){
            if(owner.getId() != userId){
                throw new GameException("Can not start game - user is not an owner");
            }
            if(gameStarted){
                throw new GameException("Game already started");
            }
            startGame();

        }
        public void startGame(){
            prepareGame();
            gameStarted = true;
        }
        public UUID getGameId() {
            return gameId;
        }

        public List<GamePlayer> getPlayers() {
            return players.values().stream().toList();
        }

        public void setGameSetup(GameSetup gameSetup) {
            this.gameSetup = gameSetup;
        }
        public void addPlayer(GamePlayer player) {
            if(players.containsKey(player.getId())) {
                throw new GameException("player already in the game");
            }
            if(players.size() >= MAX_SIZE){
                throw new GameException("max player reached");
            }
            player.setActive(true);
            player.setGameId(this.gameId.toString());
            players.put(player.getId(), player);
        }
        private void prepareGame(){
            gameSetup.generateQuestions();
            players.values().stream().forEach(p->{
                p.setQuestionCounter(gameSetup.getQuestionAmount());
            });
        }

        public Question getNextQuestionForPlayer(String playerId) throws GamePlayerNotFoundException {
             var p = players.get(playerId);
             if(p==null) throw new GameException("player not found");

             Question question = gameSetup.getNextQuestion(p.getQuestionCounter());
             if(question == null) {
                 throw new GameException("no next question");
             }

             p.setQuestionCounter(p.getQuestionCounter() - 1);

             return question;
         }
        public void endGameForAllPlayers() {
            players.values().stream().forEach(p->p.setActive(false));
        }
        public void endGameForPlayer(GamePlayer player) {
            players.get(player.getId()).setActive(false);
            players.remove(player.getId());
        }
        public boolean isGameOver(){
            return !players.values().stream().anyMatch(p->p.getQuestionCounter() > 0);

        }

     public boolean isGameStarted() {
         return gameStarted;
     }

     public Question getPlayerCurrentQuestion(GamePlayer player) {
         var p = players.get(player.getId());
         if(p == null){
             throw new GameException("player not found");
         }

         Question question = gameSetup.getNextQuestion(p.getQuestionCounter());
         if (question != null) {
             throw new GameException("no  question");
         }
         return question;
     }
 }

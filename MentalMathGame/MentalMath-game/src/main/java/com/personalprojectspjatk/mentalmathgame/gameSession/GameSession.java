package com.personalprojectspjatk.mentalmathgame.gameSession;


import com.personalprojectspjatk.mentalmathgame.model.GamePlayer;
import com.personalprojectspjatk.mentalmathgame.contract.GameState;
import com.personalprojectspjatk.mentalmathgame.contract.Question;
import com.personalprojectspjatk.mentalmathgame.excpetions.GameException;
import com.personalprojectspjatk.mentalmathgame.service.GameSetup;

import java.util.*;
public class GameSession {
        private int gameId;
        private Set<GamePlayer> players;
        private GameSetup gameSetup;
        private final int MAX_SIZE = 5;
        private boolean gameStarted = false;

        public GameSession(int gameId) {
            this.gameId = gameId;
            this.players = new HashSet<>();
        }
        public GameState getGameState(){
            var map = new HashMap<String,Integer>();
            players.stream().map(p-> map.put(p.getName(),p.getQuestionCounter()) );
            return new GameState(map);

        }
        public void startGame(){
            prepareGame();
            gameStarted = true;
        }
        public int getGameId() {
            return gameId;
        }

        public Set<GamePlayer> getPlayers() {
            return players;
        }

        public void setGameSetup(GameSetup gameSetup) {
            this.gameSetup = gameSetup;
        }
        public void addPlayer(GamePlayer player) {

            if(players.contains(player)) {
                throw new GameException("player already in the game");
            }
            if(players.size() >= MAX_SIZE){
                throw new GameException("max player reached");
            }
            player.setQuestionCounter(gameSetup.getQuestionAmount());
            players.add(player);
        }
        private void prepareGame(){
            gameSetup.generateQuestions();
        }
     public Question getNextQuestionForPlayer(String player) {

            for (GamePlayer p : players) {
             if (p.getId() == player) {
                 Question question = gameSetup.getNextQuestion(p.getQuestionCounter());
                 if(question != null) {
                     throw new GameException("no next question");
                 }
                 p.setQuestionCounter(p.getQuestionCounter() - 1);

                 return question;
             }
         }
         throw new GameException("player not found");
         }
        public void endGameForPlayer(GamePlayer player) {
            for (GamePlayer p : players){
                if (p.equals(player)) {
                    player.setActive(false);
                }
                throw new GameException("player not found");
            }
        }
        public boolean isGameOver(){
            for(GamePlayer p : players){
                if(p.isActive()){
                    return false;
                }
            }
            return true;
        }

     public boolean isGameStarted() {
         return gameStarted;
     }

     public void setGameStarted(boolean gameStarted) {
         this.gameStarted = gameStarted;
     }

     public Question getPlayerCurrentQuestion(String player) {
         for (GamePlayer p : players) {
             if (p.getId() == player) {
                 Question question = gameSetup.getNextQuestion(p.getQuestionCounter());
                 if (question != null) {
                     throw new GameException("no  question");
                 }
                 return question;
             }

         }
         throw new GameException("player not found");
     }

 }

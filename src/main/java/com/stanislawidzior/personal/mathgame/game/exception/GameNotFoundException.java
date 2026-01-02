package com.stanislawidzior.personal.mathgame.game.exception;

public class GameNotFoundException extends RoomException{
    private String gameId;
    public GameNotFoundException(String gameId) {
        super();
        this.gameId = gameId;
    }
    @Override
    public String getMessage() {
        return "The game " + gameId + " was not found";
    }
}

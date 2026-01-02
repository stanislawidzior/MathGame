package com.stanislawidzior.personal.mathgame.game.exception;

public class GamePlayerAlreadyExistsException extends RoomException {
    private final String message = "Player not found";
    public GamePlayerAlreadyExistsException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

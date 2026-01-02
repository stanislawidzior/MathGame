package com.stanislawidzior.personal.mathgame.game.exception;

public class PlayerAlreadyInTheGameException extends RoomException {
    private final String message = "Player already in the game";
    public PlayerAlreadyInTheGameException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

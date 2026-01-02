package com.stanislawidzior.personal.mathgame.game.exception;

public class GamePlayerNotFoundException extends RoomException{
    private final String message = "Player not found";
    public GamePlayerNotFoundException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

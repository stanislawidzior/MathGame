package com.stanislawidzior.personal.mathgame.game.exception;

public class UserAlreadyInGameException extends RoomException {
    private final String message = "User is already in game";
    public UserAlreadyInGameException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

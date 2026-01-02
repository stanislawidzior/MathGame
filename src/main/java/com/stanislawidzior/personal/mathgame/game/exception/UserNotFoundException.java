package com.stanislawidzior.personal.mathgame.game.exception;

public class UserNotFoundException extends RoomException{
    private final String message = "User not found";
    public UserNotFoundException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}
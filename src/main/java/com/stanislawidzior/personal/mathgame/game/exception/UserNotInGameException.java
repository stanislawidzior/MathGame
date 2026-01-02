package com.stanislawidzior.personal.mathgame.game.exception;

public class UserNotInGameException extends RoomException {
    private String message = "User is not in game";
    public UserNotInGameException(){
        super();
    }
    @Override
    public String toString(){
        return message;
    }
}

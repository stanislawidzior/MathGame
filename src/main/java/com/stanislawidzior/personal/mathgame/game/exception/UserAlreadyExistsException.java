package com.stanislawidzior.personal.mathgame.game.exception;

public class UserAlreadyExistsException extends RoomException{
    private String message = "User already registered";
    public UserAlreadyExistsException(){
        super();
    }
    @Override
    public String toString(){
        return message;
    }
}

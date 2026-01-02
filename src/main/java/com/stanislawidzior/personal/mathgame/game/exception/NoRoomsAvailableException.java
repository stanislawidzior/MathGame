package com.stanislawidzior.personal.mathgame.game.exception;

public class NoRoomsAvailableException extends RoomException{
    private final String message = "No rooms available";
    public NoRoomsAvailableException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

package com.personalprojectspjatk.mentalmathgame.excpetions;

public class NoRoomsAvailableException extends IllegalArgumentException{
    private final String message = "No rooms available";
    public NoRoomsAvailableException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

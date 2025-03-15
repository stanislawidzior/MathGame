package com.personalprojectspjatk.mentalmathgame.excpetions;

public class GamePlayerNotFoundException extends Exception{
    private final String message = "No rooms available";
    public GamePlayerNotFoundException(){
        super();

    }
    @Override
    public String getMessage(){
        return message;
    }
}

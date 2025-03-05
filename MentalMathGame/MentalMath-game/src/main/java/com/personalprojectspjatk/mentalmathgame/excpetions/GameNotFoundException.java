package com.personalprojectspjatk.mentalmathgame.excpetions;

public class GameNotFoundException extends IllegalArgumentException{
    public GameNotFoundException(String s) {
        super(s);
    }
}

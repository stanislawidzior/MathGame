package com.stanislawidzior.personal.mathgame.game.dto;

public class GameResponse implements IGameResponse {
    private String message;
    public GameResponse(String message) {
        this.message = message;
    }
    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}

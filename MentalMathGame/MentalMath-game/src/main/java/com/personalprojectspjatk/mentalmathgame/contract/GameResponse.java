package com.personalprojectspjatk.mentalmathgame.contract;

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

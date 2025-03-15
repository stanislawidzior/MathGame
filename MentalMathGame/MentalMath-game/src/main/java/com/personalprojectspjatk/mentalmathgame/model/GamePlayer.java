package com.personalprojectspjatk.mentalmathgame.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode

public class GamePlayer {
    private String id;
    private String name;
    private int questionCounter;
    private boolean active = true;
    private int gameId;
    public GamePlayer(String id, String name, int questionCounter) {
        this.id = id;
        this.name = name;
        this.questionCounter = questionCounter;
    }

    public GamePlayer() {

    }

    public void setQuestionCounter(int questionCounter) {
        this.questionCounter = questionCounter;
    }

    public int getQuestionCounter() {
        return questionCounter;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isActive() {
        return active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

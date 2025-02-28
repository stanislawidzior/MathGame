package com.personalprojectspjatk.mentalmathgame.contract;



import com.personalprojectspjatk.mentalmathgame.model.Operations;

import java.time.LocalDateTime;

public class Question implements IGameResponse {
    private double numberA;
    private double numberB;
    private LocalDateTime timeSeen;
    private LocalDateTime timeAnswered;
    private Operations operation;
    public Question(double numberA, double numberB){
        this.numberA = numberA;
        this.numberB = numberB;
    }
    public void setOperation(Operations operation) {
        this.operation = operation;
    }

    public boolean validate(int answer) {
        switch (operation) {
            case ADDITION:
                return numberA + numberB == answer;
            case SUBTRACTION:
                return numberA - numberB == answer;
            case MULTIPLICATION:
                return numberA * numberB == answer;
            case DIVISION:
                return numberA / numberB == answer;
            default:
                return false;
        }
    }

    public double getNumberB() {
        return numberB;
    }

    public void setNumberB(double numberB) {
        this.numberB = numberB;
    }

    public double getNumberA() {
        return numberA;
    }

    public void setNumberA(double numberA) {
        this.numberA = numberA;
    }

    public LocalDateTime getTimeSeen() {
        return timeSeen;
    }

    public void setTimeSeen(LocalDateTime timeSeen) {
        this.timeSeen = timeSeen;
    }

    public LocalDateTime getTimeAnswered() {
        return timeAnswered;
    }

    public void setTimeAnswered(LocalDateTime timeAnswered) {
        this.timeAnswered = timeAnswered;
    }

    public Operations getOperation() {
        return operation;
    }
}

package com.stanislawidzior.personal.mathgame.game.model;


import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class GameSettings {
    private List<Operations> allowedOperations;
    private int questionAmount;
    private int minNumber = 0;
    private int maxNumber = 1000;
    private  int minQuestionNumber = 0;
    private  int maxQuestionNumber = 100;

    public GameSettings(List<Operations> operations, int questionAmount) {
    this.allowedOperations = operations;
    this.questionAmount = questionAmount;
    }
    private void checkConstraints(int questionAmount) {
        if (questionAmount <= minQuestionNumber || questionAmount >= maxQuestionNumber) {
            throw new IllegalArgumentException("QuestionAmount must be greater than 0");
        }
    }
}

package com.personalprojectspjatk.mentalmathgame.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GameSettingsDto {
    private int questionAmount;
    private final List<String> allowedOperations = new ArrayList<>();
    private int minNumber;
    private int maxNumber;
    private  int minQuestionNumber = 0;
    private  int maxQuestionNumber = 100;

    public GameSettingsDto() {
    }
    private void checkConstraints(int questionAmount) {
        if (questionAmount <= minQuestionNumber || questionAmount >= maxQuestionNumber) {
            throw new IllegalArgumentException("QuestionAmount must be greater than 0");
        }
    }


    public List<String> getAllowedOperations() {
        return allowedOperations;
    }

    public int getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getMaxQuestionNumber() {
        return maxQuestionNumber;
    }

    public int getMinQuestionNumber() {
        return minQuestionNumber;
    }
}

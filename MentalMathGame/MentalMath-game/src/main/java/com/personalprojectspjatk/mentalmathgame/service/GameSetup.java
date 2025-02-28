package com.personalprojectspjatk.mentalmathgame.service;


import com.personalprojectspjatk.mentalmathgame.contract.GameSettingsDto;
import com.personalprojectspjatk.mentalmathgame.contract.Question;
import com.personalprojectspjatk.mentalmathgame.model.IPrepareQuestion;
import com.personalprojectspjatk.mentalmathgame.model.QuestionFactory;

import java.util.*;

public class GameSetup {
    private List<Question> questionsSet = new ArrayList<Question>();
    private List<IPrepareQuestion> prepareQuestion = new ArrayList<>();
    private GameSettingsDto testSettings;
    private boolean gameOver = false;

    public GameSetup(GameSettingsDto gameSettingsDto ) {
        this.testSettings = gameSettingsDto;
    }
    public int getQuestionAmount(){
        return testSettings.getQuestionAmount();
    }
    public void generateQuestions() {
        for (int i = 0; i < testSettings.getQuestionAmount(); i++) {
            prepareQuestion.add(getRandomOperationPrepareQuestion());
        }
        for (var prepareQuestion : prepareQuestion) {
            questionsSet.add(prepareQuestion.prepareQuestion(testSettings.getMinNumber(), testSettings.getMaxNumber()));
        }
    }

    public Question getNextQuestion(int questionIndex){
        if(questionIndex  < 0){
            return null;
        }
        if(questionsSet.size() < questionIndex - 1){
            return null;
        }
        var question = questionsSet.get(questionIndex);
        return question;
    }

    private IPrepareQuestion getRandomOperationPrepareQuestion() {
        var randomGenerator = new Random();
        var randomNumber = randomGenerator.nextInt(0,testSettings.getAllowedOperations().size()); //it's an array containg all options
        return QuestionFactory.getOperation(testSettings.getAllowedOperations().get(randomNumber));
    }



}

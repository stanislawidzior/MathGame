package com.stanislawidzior.personal.mathgame.game.service;





import com.stanislawidzior.personal.mathgame.game.dto.Question;
import com.stanislawidzior.personal.mathgame.game.model.GameSettings;
import com.stanislawidzior.personal.mathgame.game.model.IPrepareQuestion;
import com.stanislawidzior.personal.mathgame.game.model.QuestionFactory;

import java.util.*;

public class GameSetup {
    private List<Question> questionsSet = new ArrayList<Question>();
    private List<IPrepareQuestion> prepareQuestion = new ArrayList<>();
    private GameSettings gameSettings;
    private boolean gameOver = false;

    public GameSetup(GameSettings gameSettings ) {
        this.gameSettings = gameSettings;
    }
    public int getQuestionAmount(){
        return gameSettings.getQuestionAmount();
    }
    public void generateQuestions() {
        for (int i = 0; i < gameSettings.getQuestionAmount(); i++) {
            prepareQuestion.add(getRandomOperationPrepareQuestion());
        }
        for (var prepareQuestion : prepareQuestion) {
            questionsSet.add(prepareQuestion.prepareQuestion(gameSettings.getMinNumber(), gameSettings.getMaxNumber()));
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
        var randomNumber = randomGenerator.nextInt(0, gameSettings.getAllowedOperations().size()); //it's an array containg all options
        return QuestionFactory.getOperation(String.valueOf(gameSettings.getAllowedOperations().get(randomNumber)).toLowerCase());
    }



}

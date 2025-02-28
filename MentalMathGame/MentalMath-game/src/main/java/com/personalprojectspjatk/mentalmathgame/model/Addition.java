package com.personalprojectspjatk.mentalmathgame.model;


import com.personalprojectspjatk.mentalmathgame.contract.Question;

import java.util.Random;

public class Addition implements IPrepareQuestion {
    @Override
    public Question prepareQuestion(int minNumber, int maxNumber) {
        Random rand = new Random();
        var numberA = rand.nextLong(minNumber,maxNumber); //first number, nor rules
        var numberALength = String.valueOf(numberA).length();
        var minBValue = (long)Math.floor(Math.pow(10,numberALength-1)); //get similar magnitude for sensible difficulty
        var numberB = 1d;
        if(minBValue < maxNumber){
             numberB = rand.nextLong(minBValue,maxNumber);
        }else{
             numberB = rand.nextLong(maxNumber,minBValue);
        }
        var returnQuestion = new Question(numberA, numberB);
        returnQuestion.setOperation(Operations.ADDITION);
        return returnQuestion;
    }
}

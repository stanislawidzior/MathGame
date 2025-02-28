package com.personalprojectspjatk.mentalmathgame.model;


import com.personalprojectspjatk.mentalmathgame.contract.Question;

public interface IPrepareQuestion {
    Question prepareQuestion(int maxNumber, int minNumber);

}

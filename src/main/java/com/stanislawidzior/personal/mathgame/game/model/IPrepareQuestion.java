package com.stanislawidzior.personal.mathgame.game.model;


import com.stanislawidzior.personal.mathgame.game.dto.Question;

public interface IPrepareQuestion {
    Question prepareQuestion(int maxNumber, int minNumber);

}

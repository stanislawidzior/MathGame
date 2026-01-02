package com.stanislawidzior.personal.mathgame.game.model;



import com.stanislawidzior.personal.mathgame.game.dto.Question;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class QuestionsSet {
    List<Question> questions;
    public QuestionsSet() {
        questions = new ArrayList<Question>();
    }
    public void addQuestion(Question question) {
        questions.add(question);
    }

}

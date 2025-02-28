package com.personalprojectspjatk.mentalmathgame.model;


import com.personalprojectspjatk.mentalmathgame.contract.Question;
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

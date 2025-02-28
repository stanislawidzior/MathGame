package com.personalprojectspjatk.mentalmathgame.model;

import java.util.HashMap;
import java.util.Map;

public class QuestionFactory {

    private static final Map<String, Class<? extends IPrepareQuestion>> operationMap = new HashMap<>();

    static {
        operationMap.put("addition", Addition.class);
        operationMap.put("subtraction", Subtraction.class);
        operationMap.put("multiplication", Multiplication.class);
        operationMap.put("division", Division.class);
    }

    public static IPrepareQuestion getOperation(String operationName) {
        Class<? extends IPrepareQuestion> operationClass = operationMap.get(operationName.toLowerCase());
        if (operationClass != null) {
            try {
                return operationClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
package com.stanislawidzior.personal.mathgame.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stanislawidzior.personal.mathgame.game.model.Operations;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class GameSettingsDto {
    @JsonProperty("question_amount")
    private int questionAmount;
    @JsonProperty("allowed_operations")
    private final List<Operations> allowedOperations = new ArrayList<>();

}

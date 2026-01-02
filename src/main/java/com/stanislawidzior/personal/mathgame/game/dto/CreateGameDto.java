package com.stanislawidzior.personal.mathgame.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateGameDto {
    @JsonProperty("settings")
    GameSettingsDto gameSettingsDto;
}

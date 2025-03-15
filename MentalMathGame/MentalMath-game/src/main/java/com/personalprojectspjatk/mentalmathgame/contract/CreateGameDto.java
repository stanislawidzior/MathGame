package com.personalprojectspjatk.mentalmathgame.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateGameDto {
    String playerName;
    GameSettingsDto gameSettingsDto;
}

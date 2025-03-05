package com.personalprojectspjatk.mentalmathgame.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinGameDto {
    private String userName;
    private int roomId;
}

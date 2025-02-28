package com.personalprojectspjatk.mentalmathgame.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomMessage {
    private int roomId;
    private String message;

    public RoomMessage(int roomId, String message) {
        this.roomId = roomId;
        this.message = message;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

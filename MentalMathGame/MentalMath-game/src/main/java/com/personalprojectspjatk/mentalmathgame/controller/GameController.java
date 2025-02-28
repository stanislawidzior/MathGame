package com.personalprojectspjatk.mentalmathgame.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import com.personalprojectspjatk.mentalmathgame.contract.*;
import com.personalprojectspjatk.mentalmathgame.excpetions.GameException;
import com.personalprojectspjatk.mentalmathgame.excpetions.NoRoomsAvailableException;
import com.personalprojectspjatk.mentalmathgame.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
@Slf4j
@Controller
@AllArgsConstructor
public class GameController {
    private GameSessionService gameSessionService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageExceptionHandler(NoRoomsAvailableException.class)
    @SendTo("/game/rooms")
    public ResponseEntity gameException(NoRoomsAvailableException e){
        return ResponseEntity.ok(e.getMessage());
    }

    @MessageMapping("/game.hello")
    @SendTo("/game/welcome")
    public String hello(@Payload String message){
        log.info("Hello");
        return "hello";
    }
    @MessageMapping("/game/rooms")
    @SendTo("/game/rooms")
    public List<GameRoom> getAvailableRooms(){

        return gameSessionService.getAvailableGameRooms();
    }
   @MessageMapping("/game/rooms/create")
   @SendToUser("/game")
    public RoomMessage createNewGameRoom(@Payload CreateGameDto createGameDto, @Header("simpSessionId") String sessionId){
        var id = gameSessionService.createNewGame(createGameDto.getGameSettingsDto(),createGameDto.getPlayer());
        simpMessagingTemplate.convertAndSend("/game/rooms",gameSessionService.getAvailableGameRooms());
        return new RoomMessage(id,"created a game room with id: " + id +" user id =" + sessionId);
    }

//    @MessageMapping("/game/rooms/{roomid}/status")
//    @SendTo("/game/rooms/{roomid}")
//    public Question startGame(){
//        gameSessionService.joinExistingGame()
//    }

    //    @MessageMapping("/game")
//    @SendTo("/game/topic")
//    public List<GameRoom> getAvailableRooms(){
//        return gameSessionService.getAvailableGameRooms();
//    }
}

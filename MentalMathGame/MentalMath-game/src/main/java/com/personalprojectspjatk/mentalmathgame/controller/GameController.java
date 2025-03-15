package com.personalprojectspjatk.mentalmathgame.controller;

import com.personalprojectspjatk.mentalmathgame.excpetions.GameNotFoundException;
import com.personalprojectspjatk.mentalmathgame.excpetions.GamePlayerNotFoundException;
import com.personalprojectspjatk.mentalmathgame.model.GamePlayer;
import com.personalprojectspjatk.mentalmathgame.service.RandomUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.personalprojectspjatk.mentalmathgame.contract.*;
import com.personalprojectspjatk.mentalmathgame.excpetions.NoRoomsAvailableException;
import com.personalprojectspjatk.mentalmathgame.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;
@Slf4j
@Controller
@AllArgsConstructor
public class GameController {
    private GameSessionService gameSessionService;
    private RandomUserService randomUserService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageExceptionHandler(NoRoomsAvailableException.class)
    @SendTo("/exceptions")
    public ResponseEntity noGameRooms(NoRoomsAvailableException e){
        return ResponseEntity.ok(e.getMessage());
    }
    @MessageExceptionHandler(GameNotFoundException.class)
    @SendToUser("/exceptions")
    public ResponseEntity gameRoomNotFound(GameNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @MessageExceptionHandler(GamePlayerNotFoundException.class)
    @SendToUser("/exceptions")
    public ResponseEntity gameRoomNotFound(GamePlayerNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @MessageMapping("/game.hello")
    @SendTo("/game/welcome")
    public String hello(@Payload String message){
        log.info("Hello");
        return "hello";
    }
    @MessageMapping("/game/register")
    @SendToUser("/game/rooms/register")
    public String registerUser(String userName){
        var gamePlayer = new GamePlayer();
        gamePlayer.setName(userName);
        try {
            randomUserService.setNewGamePlayerIdentifier(gamePlayer);
        }catch(Exception e){

        }
        //The client is ought to save this id for further usage of this platform
        return gamePlayer.getId();
    }


    @MessageMapping("/game/rooms")
    @SendTo("/game/rooms")
    public List<GameRoom> getAvailableRooms(){
        return gameSessionService.getAvailableGameRooms();
    }
   @MessageMapping("/game/rooms/create")
   @SendToUser("/game")
    public RoomMessage createNewGameRoom(@Payload CreateGameDto createGameDto, String sessionId) throws GamePlayerNotFoundException {
       var user = randomUserService.getGamePlayerFromSession(sessionId);
       var id = gameSessionService.createNewGame(createGameDto.getGameSettingsDto(),user);
       simpMessagingTemplate.convertAndSend("/game/rooms", gameSessionService.getAvailableGameRooms());
       return new RoomMessage(id, "created a game room with id: " + id + " user id =" + sessionId);
   }
    @MessageMapping("/game/rooms/join")
    @SendToUser("/game")
    public RoomMessage joinExistingGame( String sessionId, @Payload JoinGameDto join) throws GamePlayerNotFoundException, GameNotFoundException{
        var user = randomUserService.getGamePlayerFromSession(sessionId);
        user.setName(join.getUserName());
        user.setQuestionCounter(0);
        var id = gameSessionService.joinExistingGame(join.getRoomId(), user);
        simpMessagingTemplate.convertAndSend("/game/rooms/" + join.getRoomId(), gameSessionService.getGameStateForGame(join.getRoomId()));
        return new RoomMessage(join.getRoomId(), "Joined");
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

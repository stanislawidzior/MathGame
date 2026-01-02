package com.stanislawidzior.personal.mathgame.game.controller;


import com.stanislawidzior.personal.mathgame.game.dto.CreateGameDto;
import com.stanislawidzior.personal.mathgame.game.dto.GameRoom;
import com.stanislawidzior.personal.mathgame.game.dto.JoinGameDto;
import com.stanislawidzior.personal.mathgame.game.dto.RoomMessage;
import com.stanislawidzior.personal.mathgame.game.exception.*;
import com.stanislawidzior.personal.mathgame.game.service.GameSessionService;
import com.stanislawidzior.personal.mathgame.game.service.GameUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class GameController {
    private GameSessionService gameSessionService;
    private GameUserService randomUserService;
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageExceptionHandler(RoomException.class)
    @SendToUser("/game/exceptions")
    public String roomException(RoomException e){
        return e.getMessage();
    }



    @MessageMapping("/game.hello")
    @SendTo("/game/welcome")
    public String hello(@Payload String message){
        log.info("Hello");
        return "hello";
    }




    @MessageMapping("/game/register")
    @SendToUser("/game/rooms/register")
    public String bindUserToSession(SimpMessageHeaderAccessor headerAccessor) throws UserAlreadyExistsException, UserNotFoundException {





    }


    @MessageMapping("/game/rooms")
    @SendTo("/game/rooms")
    public List<GameRoom> getAvailableRooms() throws NoRoomsAvailableException {
        return gameSessionService.getAvailableGameRooms();
    }


    @MessageMapping("/game/rooms/create")
    @SendToUser("/game")
    public RoomMessage createNewGameRoom(@Payload CreateGameDto createGameDto,SimpMessageHeaderAccessor headerAccessor) throws UserNotFoundException, UserAlreadyInGameException, NoRoomsAvailableException {
       var user = randomUserService.requireFreeUserBySession(headerAccessor.getSessionId());
       var roomId = gameSessionService.createNewGame(createGameDto.getGameSettingsDto(),user);
       randomUserService.setUserActiveRoomFromSession(headerAccessor.getSessionId(), roomId);
       var availableGameRooms = gameSessionService.getAvailableGameRooms();
       simpMessagingTemplate.convertAndSend("/game/rooms", availableGameRooms);
       return new RoomMessage(roomId.toString(), "created a game room with id: " + roomId.toString() + " user =" + user.getName()); // session id should not be sent
   }



    @MessageMapping("/game/rooms/join")
    @SendToUser("/game")
    public RoomMessage joinExistingGame( SimpMessageHeaderAccessor headerAccessor, @Payload JoinGameDto roomId) throws UserNotFoundException, GameNotFoundException, UserAlreadyInGameException, NoRoomsAvailableException {
        var user = randomUserService.requireFreeUserBySession(headerAccessor.getSessionId());
        var id = gameSessionService.joinExistingGame(roomId.getRoomId(), user);
        randomUserService.setUserActiveRoomFromSession(headerAccessor.getSessionId(), id);

        simpMessagingTemplate.convertAndSend("/game/rooms", gameSessionService.getAvailableGameRooms());
        return new RoomMessage(id, "Joined");
    }

    @MessageMapping("/game/rooms/start")
    public RoomMessage startGame(SimpMessageHeaderAccessor headerAccessor) throws UserNotInGameException, GameNotFoundException, UserNotFoundException {
        var user = randomUserService.requireOccupiedUserBySession(headerAccessor.getSessionId());
        gameSessionService.startGame(user);

        RoomMessage msg = new RoomMessage(user.getGameRoomId(), "Game started");

        for (UUID userId : gameSessionService.getGamePlayersIdFromSession(user.getGameRoomId())) {
            try {
                var sessionId = randomUserService.getAppUserByUserId(userId).getSessionId();
                simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/game", msg);
            } catch(Exception ex){
            }
        }
        return msg;
    }

}

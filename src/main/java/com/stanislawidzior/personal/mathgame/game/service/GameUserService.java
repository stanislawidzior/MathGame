package com.stanislawidzior.personal.mathgame.game.service;


import com.stanislawidzior.personal.mathgame.game.exception.*;
import com.stanislawidzior.personal.mathgame.game.model.GameUser;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameUserService {
    private ConcurrentHashMap<String, GameUser> userBySession = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, GameUser> userById = new ConcurrentHashMap<>();




    public String setNewGuestUserIdentifierReturnId(GameUser user) throws UserAlreadyExistsException {
        var id = UUID.randomUUID();
        user.setUserId(id);
        userById.putIfAbsent(id,user);

        return id.toString();
    }
    public String recoverUserSession(Principal principal) throws UserNotFoundException {
        var user = getAppUserBySessionId(principal.getName());
        return user.getSessionId();
    }
    public GameUser getAppUserBySessionId(String sessionId) throws UserNotFoundException {
        var user = userBySession.get(sessionId);
        if(user == null){
            throw new UserNotFoundException();
        }
        return user;

    }
    public GameUser getAppUserByUserId(UUID userId) throws UserNotFoundException{
        var user = userById.get(userId);
        if(user == null){
            throw new UserNotFoundException();
        }
        return user;
    }
    public GameUser requireFreeUserBySession(String sessionId) throws UserNotFoundException, UserAlreadyInGameException {
        GameUser user = getAppUserBySessionId(sessionId);
        if (user.isInGame()) {
            throw new UserAlreadyInGameException();
        }
        return user;
    }
    public GameUser requireOccupiedUserBySession(String sessionId) throws UserNotInGameException, UserNotFoundException {
        var user = getAppUserBySessionId(sessionId);
        if(!user.isInGame()){
            throw new UserNotInGameException();
        }
        return user;
    }
    public void removeGamePlayerFromSession(String sessionId) throws GamePlayerNotFoundException{
        var removed = userBySession.remove(sessionId);
        if(removed == null){
            throw new GamePlayerNotFoundException();
        }
    }
    public void setUserActiveRoomFromSession(String sessionId, String roomId) throws UserNotFoundException {
        var user = getAppUserBySessionId(sessionId);
        user.setInGame(true);
        user.setGameRoomId(roomId);


    }


}

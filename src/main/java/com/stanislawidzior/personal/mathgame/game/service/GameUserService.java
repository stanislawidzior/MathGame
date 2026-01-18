package com.stanislawidzior.personal.mathgame.game.service;


import com.stanislawidzior.personal.mathgame.game.exception.*;
import com.stanislawidzior.personal.mathgame.game.model.GameUser;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameUserService {
    private ConcurrentHashMap<UUID, GameUser> userById = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, GameUser> disconnectedUsers = new ConcurrentHashMap<>();



    public String setNewGuestUserIdReturnId(GameUser user) throws UserAlreadyExistsException {
        var id = UUID.randomUUID();
        user.setUserId(id);
        if(userById.putIfAbsent(id,user) != null){
            throw new UserAlreadyExistsException();
        }
        return id.toString();
    }
    public boolean existsUserById(UUID id) {
        return userById.containsKey(id);
    }
    public void registerOrRecoverGameUserSession(String gameUserId, String wsSessionId) throws UserNotFoundException {
        var user = disconnectedUsers.get(gameUserId);
        if(user == null){
            user.setSessionId(wsSessionId);
            userById.put(UUID.fromString(gameUserId),user);
        }else{
            user.setSessionId(wsSessionId);
        }
    }
    public GameUser getGameUserByUUID(UUID userId) throws UserNotFoundException {
        var user = userById.get(userId);
        if(user == null){
            throw new UserNotFoundException();
        }
        return user;
    }
    public GameUser requireFreeGameUserById(String userId) throws UserNotFoundException, UserAlreadyInGameException {
        GameUser user = getGameUserByUUID(UUID.fromString(userId));
        if (user.isInGame()) {
            throw new UserAlreadyInGameException();
        }
        return user;
    }
    public GameUser requireOccupiedUserById(String userId) throws UserNotInGameException, UserNotFoundException {
        var user = getGameUserByUUID(UUID.fromString(userId));
        if(!user.isInGame()){
            throw new UserNotInGameException();
        }
        return user;
    }
    public void removeGamePlayerSession(String userId) throws UserNotFoundException {
        var user = getGameUserByUUID(UUID.fromString(userId));
        if(user.isInGame()){
            disconnectedUsers.put(user.getUserId(),user);
        }
        userById.remove(UUID.fromString(userId));
    }
    public void setUserActiveRoomFromSession(String userId, String roomId) throws UserNotFoundException {
        var user = getGameUserByUUID(UUID.fromString(userId));
        user.setInGame(true);
        user.setGameRoomId(roomId);

    }


}

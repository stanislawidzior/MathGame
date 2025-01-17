package com.personalprojectspjatk.mentalmathdata.repositories;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class DataCatalog implements IDataCatalog {
    private UserRepository userRepository;
    private GameRepository gameRepository;
    //private GameHistoryRepository gameHistoryRepository;
    private NotificationsRepository notificationsRepository;
    private PlayerRepository playerRepository;
    private UserProfileRepository profileRepository;
    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public GameRepository getGameRepository() {
        return gameRepository;
    }

//    @Override
//    public GameHistoryRepository getGameHistoryRepository() {
//        return gameHistoryRepository;
//    }

    @Override
    public NotificationsRepository getNotificationsRepository() {
        return notificationsRepository;
    }

    @Override
    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    @Override
    public UserProfileRepository getUserProfileRepository() {
        return profileRepository;
    }
}

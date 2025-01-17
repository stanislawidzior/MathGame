package com.personalprojectspjatk.mentalmathdata.repositories;

public interface IDataCatalog {
    UserRepository getUserRepository();
    GameRepository getGameRepository();
   // GameHistoryRepository getGameHistoryRepository();
    NotificationsRepository getNotificationsRepository();
    PlayerRepository getPlayerRepository();
    UserProfileRepository getUserProfileRepository();
}

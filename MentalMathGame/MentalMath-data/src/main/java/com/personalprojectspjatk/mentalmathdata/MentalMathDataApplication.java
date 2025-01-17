package com.personalprojectspjatk.mentalmathdata;

import com.personalprojectspjatk.mentalmathdata.model.*;
import com.personalprojectspjatk.mentalmathdata.repositories.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.time.Duration;
import java.util.ArrayList;

@SpringBootApplication
@Slf4j
public class MentalMathDataApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public static void main(String[] args) {
        SpringApplication.run(MentalMathDataApplication.class, args);
    }
    private void createUser(){
        var user = new AppUser();
        user.setName("janek");
        user.setPassword("123");
        userRepository.save(user);

    }
    @Transactional
    public void startGame(){
        var game = new Game();
        var user = userRepository.findById(1L).get();
        var players = new ArrayList<Player>();
        var player = new Player();
        player.setAppUser(user);
        player.setGame(game);
        players.add(player);

        game.setQuestionsAmount(10);
        player.setGameDuration(Duration.ofSeconds(10));

        game.setPlayers(players);
        game.setUserProfile(user.getUserProfile());
        gameRepository.save(game);
        playerRepository.save(player);

    }
    private void setUserProfile(){
        var user = userRepository.findById(1L);
        var userdb = user.get();
        var userProfile = new UserProfile();
        userProfile.setDescription("tralala");
        userdb.setUserProfile(userProfile);
        userRepository.save(userdb);
    }

    private void updateProfile(){
        var usersProfiles = userProfileRepository.findAll();
        var profile = usersProfiles.getFirst();

        profile.setDescription("hehehe");
        userProfileRepository.save(profile);
    }

    @Override
    public void run(String... args) throws Exception {

        createUser();
        setUserProfile();
        var usersProfiles = userProfileRepository.findAll();
        log.info(usersProfiles.get(0).getUser().getName());
        log.info(userRepository.findById(1L).get().getName());
        updateProfile();
        startGame();
        var userProfile = userProfileRepository.findById(1L).get();
        var userProfileGames = userProfile.getGameHistory().getFirst();
        log.info(playerRepository.findAll().getFirst().toString());






    }
}

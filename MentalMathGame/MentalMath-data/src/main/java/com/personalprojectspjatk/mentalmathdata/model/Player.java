package com.personalprojectspjatk.mentalmathdata.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;

@Data
@Entity
@Table(name="game_player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private AppUser appUser;
    @ManyToOne()
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;
    private Duration gameDuration;
}

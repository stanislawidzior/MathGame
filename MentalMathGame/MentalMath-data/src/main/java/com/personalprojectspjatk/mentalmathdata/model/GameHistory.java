package com.personalprojectspjatk.mentalmathdata.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//        @Entity
//        @Data
//        public class GameHistory {
//            @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//            @Column(name = "id")
//            private int id;
//            @OneToMany(mappedBy ="gameHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//            private List<Game> games = new ArrayList<Game>();
//            @OneToOne(mappedBy = "gameHistory")
//            private UserProfile userProfile;
//
//
//    }

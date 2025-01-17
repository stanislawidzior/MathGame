package com.personalprojectspjatk.mentalmathdata.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @OneToOne(mappedBy = "userProfile" )
    private AppUser user;
    private String description;
    @OneToMany(mappedBy = "userProfile", fetch = FetchType.EAGER)
    private List<Game> gameHistory;
}

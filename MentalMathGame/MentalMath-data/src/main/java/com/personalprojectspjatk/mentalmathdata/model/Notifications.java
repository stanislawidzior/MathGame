package com.personalprojectspjatk.mentalmathdata.model;

import jakarta.persistence.*;

@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne()
    private AppUser receiver;
    private String content;
    private boolean seen;


}

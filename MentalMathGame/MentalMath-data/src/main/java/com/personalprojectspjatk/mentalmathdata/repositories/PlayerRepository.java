package com.personalprojectspjatk.mentalmathdata.repositories;

import com.personalprojectspjatk.mentalmathdata.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

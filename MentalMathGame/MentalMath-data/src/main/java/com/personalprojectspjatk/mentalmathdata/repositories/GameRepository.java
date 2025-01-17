package com.personalprojectspjatk.mentalmathdata.repositories;

import com.personalprojectspjatk.mentalmathdata.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Long> {
}

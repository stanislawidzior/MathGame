package com.personalprojectspjatk.mentalmathdata.repositories;

import com.personalprojectspjatk.mentalmathdata.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
   // Optional<AppUser> findByEmail(String email);
}

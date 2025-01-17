package com.personalprojectspjatk.mentalmathdata.repositories;

import com.personalprojectspjatk.mentalmathdata.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findById(Long id);
}

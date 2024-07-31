package com.globallogic.userstorage.persistence.repository;

import com.globallogic.userstorage.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByTokenAndActive(String token, boolean active);

    @Modifying
    @Query("update User u set u.token = ?1, u.lastLoginDate = ?2, u.updatedDate = ?3 where u.id = ?4")
    int updateLoginDetails(String newToken, Instant lastLoginDate, Instant updateDate, int id);

}

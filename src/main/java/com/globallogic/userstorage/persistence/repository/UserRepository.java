package com.globallogic.userstorage.persistence.repository;

import com.globallogic.userstorage.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

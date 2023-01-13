package com.dungzi.backend.domain.user.dao;

import com.dungzi.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends JpaRepository<User, String> {
    Optional<User> findByUserId(UUID userId);

    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
}
package com.dunji.backend.domain.user.dao;

import com.dunji.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {
}
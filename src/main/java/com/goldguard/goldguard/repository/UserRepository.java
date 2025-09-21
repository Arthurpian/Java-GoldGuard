package com.goldguard.goldguard.repository;

import com.goldguard.goldguard.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> { }
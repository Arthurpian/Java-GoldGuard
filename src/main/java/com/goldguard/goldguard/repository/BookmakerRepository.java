package com.goldguard.goldguard.repository;

import com.goldguard.goldguard.domain.entity.Bookmaker;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface BookmakerRepository extends JpaRepository<Bookmaker, Long> {
    Optional<Bookmaker> findByNameIgnoreCase(String name);
}

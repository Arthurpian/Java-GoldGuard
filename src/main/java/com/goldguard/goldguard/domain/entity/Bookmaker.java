package com.goldguard.goldguard.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="bookmakers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bookmaker {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true, length=120)
    private String name;
}
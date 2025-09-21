package com.goldguard.goldguard.domain.entity;

import com.goldguard.goldguard.domain.enums.TransactionType;
import com.goldguard.goldguard.domain.vo.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity @Table(name="transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="bookmaker_id")
    private Bookmaker bookmaker;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private TransactionType type;

    @Embedded
    private Money money;

    @Column(name="occurred_at", nullable=false)
    private OffsetDateTime occurredAt;

    @Column(length=500)
    private String note;
}
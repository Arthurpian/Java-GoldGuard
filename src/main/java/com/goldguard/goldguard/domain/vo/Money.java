package com.goldguard.goldguard.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Money {
    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal value;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency; // "BRL"
}

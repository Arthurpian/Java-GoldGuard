package com.goldguard.goldguard.web.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MetricsSummaryDTO(
        Long userId,
        OffsetDateTime periodStart,
        OffsetDateTime periodEnd,
        BigDecimal totalDeposits,
        BigDecimal totalWithdrawals,
        BigDecimal net,            // deposits - withdrawals
        String riskLevel,          // LOW/MEDIUM/HIGH
        BigDecimal avgPerDay
) {}
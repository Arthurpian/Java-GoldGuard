package com.goldguard.goldguard.service;

import com.goldguard.goldguard.domain.enums.TransactionType;
import com.goldguard.goldguard.repository.TransactionRepository;
import com.goldguard.goldguard.web.dto.MetricsSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service @RequiredArgsConstructor
public class MetricsService {
    private final TransactionRepository txRepo;

    @Transactional(readOnly = true)
    public MetricsSummaryDTO summarize(Long userId, OffsetDateTime start, OffsetDateTime end) {
        BigDecimal deps = txRepo.sumByTypeAndPeriod(userId, TransactionType.DEPOSIT, start, end);
        BigDecimal wds  = txRepo.sumByTypeAndPeriod(userId, TransactionType.WITHDRAWAL, start, end);
        BigDecimal net = deps.subtract(wds);

        long days = Math.max(1, Duration.between(start, end).toDays());
        BigDecimal avgPerDay = net.divide(BigDecimal.valueOf(days), java.math.MathContext.DECIMAL64);

        String risk = net.compareTo(BigDecimal.valueOf(500)) > 0 ? "HIGH"
                : net.compareTo(BigDecimal.valueOf(200)) > 0 ? "MEDIUM" : "LOW";

        return new MetricsSummaryDTO(userId, start.withOffsetSameInstant(ZoneOffset.UTC),
                end.withOffsetSameInstant(ZoneOffset.UTC),
                deps, wds, net, risk, avgPerDay);
    }
}

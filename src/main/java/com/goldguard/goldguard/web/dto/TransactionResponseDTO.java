package com.goldguard.goldguard.web.dto;

import com.goldguard.goldguard.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionResponseDTO(
        Long id,
        Long userId,
        String userName,
        Long bookmakerId,
        String bookmakerName,
        TransactionType type,
        BigDecimal amount,
        String currency,
        OffsetDateTime occurredAt,
        String note
) {}
package com.goldguard.goldguard.web.dto;

import com.goldguard.goldguard.domain.enums.TransactionType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionCreateDTO(
        @NotNull Long userId,
        @NotNull Long bookmakerId,
        @NotNull TransactionType type,
        @NotNull @DecimalMin(value="0.01") BigDecimal amount,
        @NotBlank @Size(min=3,max=3) String currency, // "BRL"
        @NotNull OffsetDateTime occurredAt,
        @Size(max=500) String note
) {}
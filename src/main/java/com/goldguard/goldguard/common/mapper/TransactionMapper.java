package com.goldguard.goldguard.common.mapper;

import com.goldguard.goldguard.domain.entity.Transaction;
import com.goldguard.goldguard.web.dto.TransactionResponseDTO;

public class TransactionMapper {
    public static TransactionResponseDTO toDTO(Transaction t) {
        return new TransactionResponseDTO(
                t.getId(),
                t.getUser().getId(),
                t.getUser().getName(),
                t.getBookmaker().getId(),
                t.getBookmaker().getName(),
                t.getType(),
                t.getMoney().getValue(),
                t.getMoney().getCurrency(),
                t.getOccurredAt(),
                t.getNote()
        );
    }
}

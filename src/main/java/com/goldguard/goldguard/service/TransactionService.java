package com.goldguard.goldguard.service;

import com.goldguard.goldguard.common.exception.BusinessException;
import com.goldguard.goldguard.common.mapper.TransactionMapper;
import com.goldguard.goldguard.domain.entity.*;
import com.goldguard.goldguard.domain.enums.TransactionType;
import com.goldguard.goldguard.domain.vo.Money;
import com.goldguard.goldguard.repository.*;
import com.goldguard.goldguard.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository txRepo;
    private final UserRepository userRepo;
    private final BookmakerRepository bmRepo;

    @Transactional
    public TransactionResponseDTO create(TransactionCreateDTO dto) {
        User user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Bookmaker bm = bmRepo.findById(dto.bookmakerId())
                .orElseThrow(() -> new BusinessException("Casa de aposta não encontrada"));

        Transaction tx = Transaction.builder()
                .user(user)
                .bookmaker(bm)
                .type(dto.type())
                .money(new Money(dto.amount(), dto.currency()))
                .occurredAt(dto.occurredAt())
                .note(dto.note())
                .build();

        return TransactionMapper.toDTO(txRepo.save(tx));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> listByPeriod(Long userId, OffsetDateTime start, OffsetDateTime end) {
        return txRepo.findByUserIdAndOccurredAtBetweenOrderByOccurredAtDesc(userId, start, end)
                .stream().map(TransactionMapper::toDTO).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!txRepo.existsById(id)) throw new BusinessException("Transação não encontrada");
        txRepo.deleteById(id);
    }
}
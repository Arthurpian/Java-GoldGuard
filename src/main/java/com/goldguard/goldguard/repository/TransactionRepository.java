package com.goldguard.goldguard.repository;

import com.goldguard.goldguard.domain.entity.Transaction;
import com.goldguard.goldguard.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdAndOccurredAtBetweenOrderByOccurredAtDesc(
            Long userId, OffsetDateTime start, OffsetDateTime end);

    @Query("""
    select coalesce(sum(t.money.value),0)
    from Transaction t
    where t.user.id = :userId
      and t.type = :type
      and t.occurredAt between :start and :end
  """)
    java.math.BigDecimal sumByTypeAndPeriod(Long userId, TransactionType type,
                                            OffsetDateTime start, OffsetDateTime end);
}
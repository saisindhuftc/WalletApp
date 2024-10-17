package com.example.walletapplication.repository;

import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IntraTransactionRepository extends JpaRepository<IntraTransaction, Integer> {

    @Query("SELECT t FROM IntraTransaction t WHERE t.id = :walletId AND t.type = :type")
    List<IntraTransaction> findByWalletId(@Param("walletId") Long walletId, @Param("type") TransactionType type);

    @Query("SELECT t FROM IntraTransaction t WHERE t.id = :walletId AND t.timestamp BETWEEN :startDate AND :endDate")
    List<IntraTransaction> findByWalletIdAndDate(@Param("walletId") Long walletId,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
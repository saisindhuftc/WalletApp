package com.example.walletapplication.repository;

import com.example.walletapplication.entity.InterTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InterTransactionRepository extends JpaRepository<InterTransaction, Long> {

    // Fetch transactions by senderWalletId within a date range
    @Query("SELECT it FROM InterTransaction it WHERE it.id = :senderWalletId AND it.timestamp BETWEEN :startDate AND :endDate")
    List<InterTransaction> findBySenderWalletIdAndDate(Long senderWalletId, LocalDateTime startDate, LocalDateTime endDate);

    // Fetch transactions by receiverWalletId within a date range
    @Query("SELECT it FROM InterTransaction it WHERE it.id = :receiverWalletId AND it.timestamp BETWEEN :startDate AND :endDate")
    List<InterTransaction> findByReceiverWalletIdAndDate(Long receiverWalletId, LocalDateTime startDate, LocalDateTime endDate);
}
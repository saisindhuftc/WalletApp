package com.example.walletapplication.repository;

import com.example.walletapplication.entity.IntraWalletTransaction;
import com.example.walletapplication.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IntraWalletTransactionRepository extends JpaRepository<IntraWalletTransaction, Integer> {
    @Query("SELECT i FROM IntraWalletTransaction i WHERE i.wallet.id IN (?1)")
    List<IntraWalletTransaction> findByWallets(List<Integer> wallets);

    @Query("SELECT i FROM IntraWalletTransaction i WHERE i.wallet.id = :walletId AND i.timestamp BETWEEN :startDate AND :endDate")
    List<IntraWalletTransaction> findByWalletIdAndDate(Long walletId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT i FROM IntraWalletTransaction i WHERE i.wallet.id = ?1 AND i.type = ?2")
    List<IntraWalletTransaction> findByWalletIdAndType(Long walletId, TransactionType type);
}
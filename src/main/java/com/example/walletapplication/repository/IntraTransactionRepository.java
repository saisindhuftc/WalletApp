package com.example.walletapplication.repository;

import com.example.walletapplication.entity.IntraTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface IntraTransactionRepository extends JpaRepository<IntraTransaction, Integer> {
    List<IntraTransaction> findByWallets(List<Integer> wallets);

    List<IntraTransaction> findByWalletsAndDate(List<Integer> wallets, LocalDateTime startDate, LocalDateTime endDate);
}

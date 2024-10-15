package com.example.walletapplication.repository;

import com.example.walletapplication.entity.IntraTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface IntraTransactionRepository extends JpaRepository<IntraTransaction, Long> {
    List<IntraTransaction> findByWallets(List<Long> wallets);

    List<IntraTransaction> findByWalletsAndDate(List<Long> wallets, LocalDateTime startDate, LocalDateTime endDate);
}

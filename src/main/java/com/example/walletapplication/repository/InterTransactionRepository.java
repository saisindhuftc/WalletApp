package com.example.walletapplication.repository;

import com.example.walletapplication.entity.InterTransaction;
import com.example.walletapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InterTransactionRepository extends JpaRepository<InterTransaction, Long> {

    public List<InterTransaction> findTransactionsOfUser(User user);

    public List<InterTransaction> findTransactionsOfUserDateBased(User user, LocalDateTime startDate, LocalDateTime endDate);
}

package com.example.walletapplication.repository;

import com.example.walletapplication.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
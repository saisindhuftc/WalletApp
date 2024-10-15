package com.example.walletapplication.repository;

import com.example.walletapplication.entity.CurrencyValue;
import com.example.walletapplication.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyValue, Currency> {
}
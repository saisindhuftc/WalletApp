package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance = 0.0;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    public Wallet(CurrencyType currency) {
        this.currency = currency;
    }

    public void deposit(double amount, CurrencyType depositCurrency) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount should be greater than 0");
        }
        double convertedAmount = CurrencyConverter.convertMoney(amount, depositCurrency, this.currency);
        this.balance += convertedAmount;
    }

    public void withdraw(double amount, CurrencyType withdrawalCurrency) throws InsufficientBalanceException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount should be greater than 0");
        }
        double convertedAmount = CurrencyConverter.convertMoney(amount, withdrawalCurrency, this.currency);

        if (convertedAmount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        this.balance -= convertedAmount;
    }
}
package com.example.walletapplication.entity;

import com.example.walletapplication.enums.Currency;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Money {

    private double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    // Method to add money, handling currency conversion
    public void add(Money money) throws InvalidAmountException {
        double convertedAmount = CurrencyConverter.convertMoney(money, money.getCurrency(), this.currency).getAmount();
        if (convertedAmount <= 0) {
            throw new InvalidAmountException("Deposit should be greater than 0");
        }
        this.amount += convertedAmount;
    }

    // Method to subtract money, handling currency conversion
    public void subtract(Money money) throws InvalidAmountException, InsufficientBalanceException {
        double convertedAmount = CurrencyConverter.convertMoney(money, money.getCurrency(), this.currency).getAmount();

        if (convertedAmount > this.amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        if (convertedAmount <= 0) {
            throw new InvalidAmountException("Withdrawal should be greater than 0");
        }

        this.amount -= convertedAmount;
    }
}

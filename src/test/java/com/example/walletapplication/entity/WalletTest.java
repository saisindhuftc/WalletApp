package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void testWalletInitialization() {
        CurrencyType currencyType = CurrencyType.USD;
        Wallet wallet = new Wallet(currencyType);

        assertEquals(0.0, wallet.getBalance());
        assertEquals(currencyType, wallet.getCurrency());
    }

    @Test
    void testDepositSuccess() throws InvalidAmountException {
        Wallet wallet = new Wallet(CurrencyType.USD);
        wallet.deposit(100.0, CurrencyType.USD);

        assertEquals(100.0, wallet.getBalance());
    }

    @Test
    void testDepositInvalidAmount() {
        Wallet wallet = new Wallet(CurrencyType.USD);

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            wallet.deposit(-50.0, CurrencyType.USD);
        });
        assertEquals("Deposit amount should be greater than 0", exception.getMessage());
    }

    @Test
    void testWithdrawSuccess() throws InvalidAmountException, InsufficientBalanceException {
        Wallet wallet = new Wallet(CurrencyType.USD);
        wallet.deposit(100.0, CurrencyType.USD);
        wallet.withdraw(50.0, CurrencyType.USD);

        assertEquals(50.0, wallet.getBalance());
    }

    @Test
    void testWithdrawInvalidAmount() {
        Wallet wallet = new Wallet(CurrencyType.USD);

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            wallet.withdraw(-50.0, CurrencyType.USD);
        });
        assertEquals("Withdrawal amount should be greater than 0", exception.getMessage());
    }

    @Test
    void testWithdrawInsufficientBalance() throws InvalidAmountException {
        Wallet wallet = new Wallet(CurrencyType.USD);
        wallet.deposit(50.0, CurrencyType.USD);

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            wallet.withdraw(100.0, CurrencyType.USD);
        });
        assertEquals("Insufficient balance", exception.getMessage());
    }

}
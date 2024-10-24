package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import com.example.walletapplication.service.CurrencyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WalletTest {
    private CurrencyConverter currencyConverter;

    @BeforeEach
    void setUp() {
        currencyConverter = mock(CurrencyConverter.class);
    }

    @Test
    void testWalletInitialization() {
        CurrencyType currencyType = CurrencyType.USD;
        Wallet wallet = new Wallet(currencyType);

        assertEquals(0.0, wallet.getBalance());
        assertEquals(currencyType, wallet.getCurrency());
    }

    @Test
    void testDepositSuccess1() throws InvalidAmountException {
        when(currencyConverter.convertMoney(100.0, CurrencyType.USD, CurrencyType.USD)).thenReturn(100.0);
        Wallet wallet = new Wallet(CurrencyType.USD, currencyConverter);

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
        // Mock the conversion behavior
        when(currencyConverter.convertMoney(100.0, CurrencyType.USD, CurrencyType.INR)).thenReturn(100.0);
        when(currencyConverter.convertMoney(50.0, CurrencyType.USD, CurrencyType.INR)).thenReturn(50.0);

        // Pass the mocked currency converter into the Wallet
        Wallet wallet = new Wallet(CurrencyType.INR, currencyConverter);

        // Now the deposit and withdraw will use the mocked CurrencyConverter
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
        when(currencyConverter.convertMoney(100.0, CurrencyType.USD, CurrencyType.USD)).thenReturn(100.0);

        Wallet wallet = new Wallet(CurrencyType.USD, currencyConverter);
        wallet.deposit(50.0, CurrencyType.USD);

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            wallet.withdraw(100.0, CurrencyType.USD);
        });

        assertEquals("Insufficient balance", exception.getMessage());
    }
}

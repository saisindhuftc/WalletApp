package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    public void testWalletCreation() {
        Wallet wallet = new Wallet();
        assertNotNull(wallet);
    }

    //Tests for Deposi()

    @Test
    public void testDepositValidAmount() {
        Wallet wallet = new Wallet(new User("sai", "password456"));
        double newBalance = wallet.deposit(150.0);
        assertEquals(150.0, newBalance);
    }

    @Test
    public void testDepositInvalidAmount() {
        Wallet wallet = new Wallet(new User("pooji", "password200"));
        Exception exception = assertThrows(InvalidAmountException.class, () -> {
            wallet.deposit(-50.0);
        });
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    //Tests for Withdraw()

    @Test
    public void testWithdrawNegativeAmount() {
        Wallet wallet = new Wallet(new User("lahari", "password100"));
        Exception exception = assertThrows(InvalidAmountException.class, () -> {
            wallet.withdraw(-10.0);
        });
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    public void testWithdrawZeroAmount() {
        Wallet wallet = new Wallet(new User("sindhu", "password123"));
        Exception exception = assertThrows(InvalidAmountException.class, () -> {
            wallet.withdraw(0.0);
        });
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    public void testWithdrawValidAmount() {
        Wallet wallet = new Wallet(new User("pooji", "password200"));
        wallet.deposit(200.0);
        double newBalance = wallet.withdraw(100.0);
        assertEquals(100.0, newBalance);
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        Wallet wallet = new Wallet(new User("nas", "password789"));
        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            wallet.withdraw(100.0);
        });
        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    public void testWithdrawInvalidAmount() {
        Wallet wallet = new Wallet(new User("lahari", "password100"));
        Exception exception = assertThrows(InvalidAmountException.class, () -> {
            wallet.withdraw(-10.0);
        });
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    public void testTwoWalletCreations(){
        Wallet wallet1 = new Wallet(new User("sai", "password456"));
        Wallet wallet2 = new Wallet(new User("pooji", "password200"));
        assertNotNull(wallet1);
        assertNotNull(wallet2);
    }
}

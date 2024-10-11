package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    public void testWalletCreation() {
        Wallet wallet = new Wallet();
        assertNotNull(wallet);
    }

    @Test
    public void testWalletBalanceAfterCreation() {
        Wallet wallet = new Wallet();
        assertEquals(0.0, wallet.getBalance());
    }

    @Test
    public void testValidWalletId() {
        Wallet wallet = new Wallet();
        assertNull(wallet.getId());
    }

    @Test
    public void testInValidWalletId() {
        Wallet wallet = new Wallet();
        assertNull(wallet.getId());
    }

    @Test
    public void testValidBalance() {
        Wallet wallet = new Wallet();
        wallet.deposit(200.0);
        assertEquals(200.0, wallet.getBalance());
    }

    @Test
    public void testInValidBalance() {
        Wallet wallet = new Wallet();
        wallet.deposit(200.0);
        assertNotEquals(300.0, wallet.getBalance());
    }

    @Test
    public void testWalletSetBalance() {
        Wallet wallet = new Wallet();
        wallet.deposit(150.0);
        assertEquals(150.0, wallet.getBalance());
    }

    @Test
    public void testTwoWalletCreations() {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        assertNotEquals(wallet1, wallet2);
    }

    @Test
    public void testWithdraw() {
        Wallet wallet = new Wallet();
        wallet.deposit(100.0);
        wallet.withdraw(50.0);
        assertEquals(50.0, wallet.getBalance());
    }

    @Test
    public void testWithdrawNegativeAmount() {
        Wallet wallet = new Wallet();
        assertThrows(InvalidAmountException.class, () -> {
            wallet.withdraw(-50.0);
        });
    }

    @Test
    public void testWithdrawZeroAmount() {
        Wallet wallet = new Wallet();
        assertThrows(InvalidAmountException.class, () -> {
            wallet.withdraw(0.0);
        });
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        Wallet wallet = new Wallet();
        wallet.deposit(50.0);
        assertThrows(InsufficientBalanceException.class, () -> {
            wallet.withdraw(100.0);
        });
    }

    @Test
    public void testDeposit() {
        Wallet wallet = new Wallet();
        wallet.deposit(100.0);
        assertEquals(100.0, wallet.getBalance());
    }


    @Test
    public void testDepositInvalidAmount() {
        Wallet wallet = new Wallet();
        assertThrows(InvalidAmountException.class, () -> {
            wallet.deposit(-50.0);
        });
    }
}
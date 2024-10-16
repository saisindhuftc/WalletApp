package com.example.walletapplication.entity;
import com.example.walletapplication.enums.Country;
import com.example.walletapplication.enums.Currency;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class WalletTest {

    @Test
    void expectExceptionForInvalidAmountDeposited() throws InvalidAmountException {
        Wallet wallet = new Wallet(Country.INDIA);
        assertThrows(InvalidAmountException.class,()-> wallet.deposit(new Money(-50, Currency.INR)));
    }

    @Test
    void expectMoneyWithdrawn() throws InsufficientBalanceException, InvalidAmountException {
        Money money = mock(Money.class);
        Money moneyToAdd = new Money(100, Currency.INR);
        Money moneyToWithdraw = new Money(50, Currency.INR);

        Wallet wallet = new Wallet(1, money);

        wallet.deposit(moneyToAdd);
        wallet.withdraw(moneyToWithdraw);

        verify(money, times(1)).add(moneyToAdd);
        verify(money, times(1)).subtract(moneyToWithdraw);

    }

    @Test
    void expectExceptionForInsufficientBalanceWhenWithdrawing() throws InsufficientBalanceException, InvalidAmountException {
        Wallet wallet = new Wallet(Country.INDIA);
        assertThrows(InsufficientBalanceException.class, ()-> wallet.withdraw(new Money(100, Currency.INR)));
    }

    @Test
    void expectExceptionForInvalidAmountWithdrawn() throws InvalidAmountException {
        Wallet wallet = new Wallet(Country.INDIA);
        assertThrows(InvalidAmountException.class,()-> wallet.withdraw(new Money(-50, Currency.INR)));
    }

    @Test
    void expectCurrencyINRForIndia() {
        Wallet wallet = new Wallet(Country.INDIA);

        assertEquals(new Money(0.0,Currency.INR), wallet.getMoney());
    }

    @Test
    void expectCurrencyUSDForUSA() {
        Wallet wallet = new Wallet(Country.USA);

        assertEquals(new Money(0.0,Currency.USD), wallet.getMoney());
    }
}
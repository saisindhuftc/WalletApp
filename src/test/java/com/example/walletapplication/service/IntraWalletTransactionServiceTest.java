package com.example.walletapplication.service;

import com.example.walletapplication.entity.IntraWalletTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.IntraWalletTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IntraWalletTransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IntraWalletTransactionRepository intraTransactionRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private IntraWalletTransactionService walletService;

    private User user;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        wallet = new Wallet();
        wallet.setId(1L);
        user.setWallet(wallet);
    }

    @Test
    public void testDepositSuccess() {
        User user = new User("testUser", "password", CurrencyType.USD);
        Wallet wallet = new Wallet(CurrencyType.USD);
        user.setWallet(wallet);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(intraTransactionRepository.save(any(IntraWalletTransaction.class))).thenReturn(new IntraWalletTransaction());

        assertDoesNotThrow(() -> walletService.deposit(1L, 100.0, CurrencyType.INR));
        verify(intraTransactionRepository, times(1)).save(any(IntraWalletTransaction.class));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testDepositFailedException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Deposit failed")).when(intraTransactionRepository).save(any(IntraWalletTransaction.class));

        assertThrows(RuntimeException.class, () -> walletService.deposit(1L, 100.0, CurrencyType.USD));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testDepositWhenUserIsUnAuthorizedException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("wronguser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(UnAuthorisedUserException.class, () -> walletService.isUserAuthorized(1L, 1L));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(authentication, times(1)).getName();
    }

    @Test
    public void testDepositWhenUnAuthorisedWalletException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(UnAuthorisedWalletException.class, () -> walletService.isUserAuthorized(1L, 2L));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(authentication, times(1)).getName();
    }

    @Test
    public void testDepositWhenUserNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.deposit(1L, 100.0, CurrencyType.USD));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testDepositInvalidAmountException() {
        assertThrows(InvalidAmountException.class, () -> walletService.deposit(1L, -100.0, CurrencyType.USD));
    }

    @Test
    public void testDefaultCurrencyTypeIsINR() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        wallet = new Wallet();
        user.setWallet(wallet);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        assertEquals(CurrencyType.INR, user.getCurrencyType());
    }

    @Test
    public void testWithdrawSuccess() {
        User user = new User("testUser", "password", CurrencyType.USD);
        Wallet wallet = new Wallet(CurrencyType.USD);
        wallet.setBalance(200.0);
        user.setWallet(wallet);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(intraTransactionRepository.save(any(IntraWalletTransaction.class))).thenReturn(new IntraWalletTransaction());

        assertDoesNotThrow(() -> walletService.withdraw(1L, 100.0, CurrencyType.USD));
        verify(intraTransactionRepository, times(1)).save(any(IntraWalletTransaction.class));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testWithdrawWhenUserIsUnAuthorizedException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("wronguser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(UnAuthorisedUserException.class, () -> walletService.isUserAuthorized(1L, 1L));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(authentication, times(1)).getName();
    }

    @Test
    public void testWithdrawWhenUnAuthorisedWalletException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(UnAuthorisedWalletException.class, () -> walletService.isUserAuthorized(1L, 2L));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(authentication, times(1)).getName();
    }

    @Test
    public void testWithdrawInvalidAmountException() {
        assertThrows(InvalidAmountException.class, () -> walletService.withdraw(1L, -100.0, CurrencyType.USD));
    }

    @Test
    public void testWithdrawFailedException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Withdrawal failed")).when(intraTransactionRepository).save(any(IntraWalletTransaction.class));

        assertThrows(RuntimeException.class, () -> walletService.withdraw(1L, 100.0, CurrencyType.USD));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testWithdrawUserNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.withdraw(1L, 100.0, CurrencyType.USD));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testWithdrawInsufficientBalanceException() {
        User user = new User("testUser", "password", CurrencyType.USD);
        Wallet wallet = mock(Wallet.class);
        user.setWallet(wallet);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(wallet).withdraw(any(Double.class), any(CurrencyType.class));

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(1L, 100.0, CurrencyType.USD));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(wallet, times(1)).withdraw(any(Double.class), any(CurrencyType.class));
    }


    @Test
    public void testGetDepositsSuccess() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT)))
                .thenReturn(List.of(new IntraWalletTransaction()));

        List<IntraWalletTransaction> deposits = walletService.getDeposits(1L);
        assertFalse(deposits.isEmpty());
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT));
    }

    @Test
    public void testGetDepositsWalletNotFound() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT)))
                .thenReturn(Collections.emptyList());

        List<IntraWalletTransaction> deposits = walletService.getDeposits(2L);
        assertTrue(deposits.isEmpty());
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT));
    }

    @Test
    public void testGetWithdrawalsWalletNotFound() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL)))
                .thenReturn(Collections.emptyList());

        List<IntraWalletTransaction> withdrawals = walletService.getWithdrawals(2L);
        assertTrue(withdrawals.isEmpty());
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL));
    }

    @Test
    public void testGetDepositsInternalError() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT)))
                .thenThrow(new RuntimeException("Internal error"));

        assertThrows(RuntimeException.class, () -> walletService.getDeposits(1L));
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT));
    }

    @Test
    public void testGetWithdrawalsInternalError() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL)))
                .thenThrow(new RuntimeException("Internal error"));

        assertThrows(RuntimeException.class, () -> walletService.getWithdrawals(1L));
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL));
    }

    @Test
    public void testGetDepositsEmpty() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT)))
                .thenReturn(Collections.emptyList());

        List<IntraWalletTransaction> deposits = walletService.getDeposits(1L);
        assertTrue(deposits.isEmpty());
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT));
    }

    @Test
    public void testGetWithdrawalsEmpty() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL)))
                .thenReturn(Collections.emptyList());

        List<IntraWalletTransaction> withdrawals = walletService.getWithdrawals(1L);
        assertTrue(withdrawals.isEmpty());
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL));
    }

    @Test
    public void testGetDepositsUserAndWalletMismatch() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.DEPOSIT)))
                .thenReturn(Collections.emptyList());

        assertThrows(UnAuthorisedWalletException.class, () -> walletService.isUserAuthorized(1L, 2L));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testGetWithdrawalsUserAndWalletMismatch() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL)))
                .thenReturn(Collections.emptyList());

        assertThrows(UnAuthorisedWalletException.class, () -> walletService.isUserAuthorized(1L, 2L));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testGetWithdrawalsSuccess() {
        when(intraTransactionRepository.findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL)))
                .thenReturn(List.of(new IntraWalletTransaction()));

        List<IntraWalletTransaction> withdrawals = walletService.getWithdrawals(1L);
        assertFalse(withdrawals.isEmpty());
        verify(intraTransactionRepository, times(1)).findByWalletIdAndType(any(Long.class), eq(TransactionType.WITHDRAWAL));
    }
}
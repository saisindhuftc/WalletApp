package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterTransaction;
import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.InterTransactionRepository;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterTransactionRepository interTransactionRepository;

    @Mock
    private IntraTransactionRepository intraTransactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransfer_Success() {
        User sender = new User("sender", "password", CurrencyType.USD);
        sender.getWallet().deposit(100.0, CurrencyType.USD);

        User receiver = new User("receiver", "password", CurrencyType.USD);
        receiver.getWallet().deposit(50.0, CurrencyType.USD);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        transactionService.transfer(1L, 2L, 30.0, CurrencyType.USD);

        verify(interTransactionRepository, times(1)).save(any(InterTransaction.class));
    }

    @Test
    void testTransfer_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transactionService.transfer(1L, 2L, 30.0, CurrencyType.USD);
        });
    }

    @Test
    void testTransfer_InsufficientBalance() {
        User sender = new User("sender", "password", CurrencyType.USD);
        sender.getWallet().deposit(20.0, CurrencyType.USD);
        User receiver = new User("receiver", "password", CurrencyType.USD);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.transfer(1L, 2L, 30.0, CurrencyType.USD);
        });
    }

    @Test
    void testGetTransactionHistory_Success() {
        Long walletId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        List<IntraTransaction> intraTransactions = Arrays.asList(
                new IntraTransaction(new Wallet(CurrencyType.USD), TransactionType.DEPOSIT, 100.0, startDate)
        );
        List<InterTransaction> senderTransactions = Arrays.asList(
                new InterTransaction(new Wallet(CurrencyType.USD), new Wallet(CurrencyType.USD), TransactionType.TRANSFER, 50.0, startDate)
        );
        List<InterTransaction> receiverTransactions = Arrays.asList(
                new InterTransaction(new Wallet(CurrencyType.USD), new Wallet(CurrencyType.USD), TransactionType.TRANSFER, 20.0, startDate)
        );

        when(intraTransactionRepository.findByWalletIdAndDate(walletId, startDate, endDate)).thenReturn(intraTransactions);
        when(interTransactionRepository.findBySenderWalletIdAndDate(walletId, startDate, endDate)).thenReturn(senderTransactions);
        when(interTransactionRepository.findByReceiverWalletIdAndDate(walletId, startDate, endDate)).thenReturn(receiverTransactions);

        List<Object> transactions = transactionService.getTransactionHistory(walletId, startDate, endDate);

        assertEquals(3, transactions.size());
        assertTrue(transactions.containsAll(intraTransactions));
        assertTrue(transactions.containsAll(senderTransactions));
        assertTrue(transactions.containsAll(receiverTransactions));
    }

    @Test
    void testGetTransactionHistoryByType_Success() {
        Long walletId = 1L;
        TransactionType type = TransactionType.DEPOSIT;

        List<IntraTransaction> intraTransactions = Arrays.asList(
                new IntraTransaction(new Wallet(CurrencyType.USD), TransactionType.DEPOSIT, 100.0, LocalDateTime.now())
        );

        when(intraTransactionRepository.findByWalletId(walletId, type)).thenReturn(intraTransactions);

        List<IntraTransaction> result = transactionService.getTransactionHistoryByType(walletId, type);

        assertEquals(1, result.size());
        assertEquals(type, result.get(0).getType());
    }
}

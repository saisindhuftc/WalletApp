package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.repository.InterTransactionRepository;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.responseModels.TransactionsResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntraTransactionServiceTest {

    @Mock
    private InterTransactionRepository interTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IntraTransactionRepository intraTransactionRepository;

    @InjectMocks
    private IntraTransactionService intraTransactionService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAllTransactionsSuccess() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        Wallet wallet = new Wallet();
        wallet.setWalletId(1);
        user.setWallets(List.of(wallet));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(intraTransactionRepository.findByWallets(List.of(1))).thenReturn(new ArrayList<>());
        when(interTransactionRepository.findTransactionsOfUser(user)).thenReturn(new ArrayList<>());

        TransactionsResponseModel response = intraTransactionService.allTransactions();

        assertNotNull(response);
        assertTrue(response.getIntraWalletTransactions().isEmpty());
        assertTrue(response.getInterWalletTransactions().isEmpty());
    }

    @Test
    void testAllTransactionsUserNotFound() {
        String username = "testUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> intraTransactionService.allTransactions());
    }

    @Test
    void testAllTransactionsDateBasedSuccess() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        Wallet wallet = new Wallet();
        wallet.setWalletId(1);
        user.setWallets(List.of(wallet));
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(intraTransactionRepository.findByWalletsAndDate(List.of(1), startDate.atTime(0, 0, 0), endDate.atTime(23, 59, 59))).thenReturn(new ArrayList<>());
        when(interTransactionRepository.findTransactionsOfUserDateBased(user, startDate.atTime(0, 0, 0), endDate.atTime(23, 59, 59))).thenReturn(new ArrayList<>());

        TransactionsResponseModel response = intraTransactionService.allTransactionsDateBased(startDate, endDate);

        assertNotNull(response);
        assertTrue(response.getIntraWalletTransactions().isEmpty());
        assertTrue(response.getInterWalletTransactions().isEmpty());
    }

    @Test
    void testAllTransactionsDateBasedUserNotFound() {
        String username = "testUser";
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> intraTransactionService.allTransactionsDateBased(startDate, endDate));
    }
}
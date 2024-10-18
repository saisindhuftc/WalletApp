package com.example.walletapplication.service;

import com.example.walletapplication.entity.IntraTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterTransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IntraTransactionRepository intraTransactionRepository;

    @InjectMocks
    private IntraTransactionService walletService;

    private User user;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet(CurrencyType.USD);
        user = new User("testuser", "password", CurrencyType.USD);
        user.setWallet(wallet);
    }

    @Test
    void testIsUserAuthorized_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.isUserAuthorized(1L, wallet.getId()));
    }

    @Test
    void testIsUserAuthorized_UnauthorizedUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("otheruser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(UnAuthorisedUserException.class, () -> walletService.isUserAuthorized(1L, wallet.getId()));
    }

    @Test
    void testDeposit_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> walletService.deposit(1L, 100.0, CurrencyType.USD));
        assertEquals(100.0, wallet.getBalance());
        verify(intraTransactionRepository, times(1)).save(any(IntraTransaction.class));
    }

    @Test
    void testDeposit_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.deposit(1L, 100.0, CurrencyType.USD));
    }

    @Test
    void testWithdraw_Success() {
        wallet.deposit(100.0, CurrencyType.USD);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> walletService.withdraw(1L, 50.0, CurrencyType.USD));
        assertEquals(50.0, wallet.getBalance());
        verify(intraTransactionRepository, times(1)).save(any(IntraTransaction.class));
    }

    @Test
    void testWithdraw_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.withdraw(1L, 50.0, CurrencyType.USD));
    }

    @Test
    void testWithdraw_InsufficientBalance() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(1L, 50.0, CurrencyType.USD));
    }
}
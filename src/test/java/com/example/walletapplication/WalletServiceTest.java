package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import com.example.walletapplication.service.WalletService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Test
    public void testDeposit() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        WalletService walletService = new WalletService(userRepository, walletRepository);
        User user = new User("lahari", "password100");
        Wallet wallet = mock(Wallet.class);

        when(userRepository.findByUsername("lahari")).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        walletService.deposit("lahari", 100.0);

        verify(wallet).deposit(100.0);
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testWithdraw() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        WalletService walletService = new WalletService(userRepository, walletRepository);
        User user = new User("nas", "password100");
        Wallet wallet = mock(Wallet.class);

        when(userRepository.findByUsername("nas")).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        walletService.withdraw("nas", 50.0);

        verify(wallet).withdraw(50.0);
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        WalletService walletService = new WalletService(userRepository, walletRepository);
        User user = new User("sindhu", "password123");
        Wallet wallet = mock(Wallet.class);

        when(userRepository.findByUsername("sindhu")).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(wallet);
        doThrow(new IllegalArgumentException("Insufficient balance")).when(wallet).withdraw(150.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            walletService.withdraw("sindhu", 150.0);
        });

        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    public void testUserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        WalletService walletService = new WalletService(userRepository, walletRepository);
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            walletService.deposit("nonexistentuser", 100.0);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testWalletNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        WalletService walletService = new WalletService(userRepository, walletRepository);
        User user = new User("testuser", "password");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(null);

    }
}

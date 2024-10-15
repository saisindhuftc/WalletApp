package com.example.walletapplication.repository;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletRepositoryTest {

    @Test
    public void testFindByUser() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        User expectedUser = new User("sai", "password123");
        Wallet expectedWallet = new Wallet();
        expectedWallet.setUser(expectedUser);

        when(walletRepository.findByUser(expectedUser)).thenReturn(expectedWallet);

        Wallet actualWallet = walletRepository.findByUser(expectedUser);

        assertNotNull(actualWallet);
        assertEquals(expectedWallet.getUser(), actualWallet.getUser());
    }

    @Test
    public void testFindByUserNotFound() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        User user = new User("nonExistentUser", "testPassword");

        when(walletRepository.findByUser(user)).thenReturn(null);

        Wallet actualWallet = walletRepository.findByUser(user);

        assertNull(actualWallet);
    }
}

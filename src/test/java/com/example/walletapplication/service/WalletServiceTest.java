package com.example.walletapplication.service;

import com.example.walletapplication.entity.Money;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.Currency;
import com.example.walletapplication.exception.AuthenticationFailedException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.IntraTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import com.example.walletapplication.requestModels.WalletRequestModel;
import com.example.walletapplication.responseModels.WalletResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private WalletService walletService;


    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(new Wallet());
        when(walletRepository.findAll()).thenReturn(wallets);

        List<WalletResponseModel> response = walletService.getAllWallets();
        assertEquals(1, response.size());
        verify(walletRepository, times(1)).findAll();
    }

    @Test
    public void testDepositUserNotFound() {
        WalletRequestModel requestModel = new WalletRequestModel(new Money(100.0, Currency.EUR));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> {
            walletService.deposit(1, "testuser", requestModel);
        });
    }

    @Test
    public void testWithdrawUserNotFound() {
        WalletRequestModel requestModel = new WalletRequestModel(new Money(50.0, Currency.USD));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> {
            walletService.withdraw(1, "testuser", requestModel);
        });
    }

}
package com.example.walletapplication.service;

import com.example.walletapplication.entity.*;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.*;
import com.example.walletapplication.requestModels.InterTransactionRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterTransactionServiceTest {

    @Mock
    private InterTransactionRepository interTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private InterTransactionService interTransactionService;

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
    public void testTransactUserNotFound() {
        String username = "testUser";
        String receiverName = "receiverUser";
        InterTransactionRequestModel requestModel = new InterTransactionRequestModel();
        requestModel.setReceiverName(receiverName);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> interTransactionService.transact(requestModel));
    }

    @Test
    public void testTransactWalletNotFound() {
        String username = "testUser";
        String receiverName = "receiverUser";
        User sender = new User();
        sender.setUsername(username);
        User receiver = new User();
        receiver.setUsername(receiverName);
        InterTransactionRequestModel requestModel = new InterTransactionRequestModel();
        requestModel.setSenderWalletId(1);
        requestModel.setReceiverWalletId(2);
        requestModel.setReceiverName(receiverName);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> interTransactionService.transact(requestModel));
    }

    @Test
    void expectReceiverWalletNotFoundExceptionOnTransaction() {
        String username = "testUser";
        String receiverName = "receiverUser";
        User sender = new User();
        sender.setUsername(username);
        User receiver = new User();
        receiver.setUsername(receiverName);
        Wallet senderWallet = new Wallet();
        InterTransactionRequestModel requestModel = new InterTransactionRequestModel();
        requestModel.setSenderWalletId(1);
        requestModel.setReceiverWalletId(2);
        requestModel.setReceiverName(receiverName);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(walletRepository.findById(1)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> interTransactionService.transact(requestModel));
    }

    @Test
    void expectSenderWalletNotFoundExceptionOnTransaction() {
        String username = "testUser";
        String receiverName = "receiverUser";
        User sender = new User();
        sender.setUsername(username);
        User receiver = new User();
        receiver.setUsername(receiverName);
        InterTransactionRequestModel requestModel = new InterTransactionRequestModel();
        requestModel.setSenderWalletId(1);
        requestModel.setReceiverWalletId(2);
        requestModel.setReceiverName(receiverName);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> interTransactionService.transact(requestModel));
    }

    @Test
    void expectReceiverNotFoundOnTransaction() {
        String username = "testUser";
        String receiverName = "receiverUser";
        User sender = new User();
        sender.setUsername(username);
        InterTransactionRequestModel requestModel = new InterTransactionRequestModel();
        requestModel.setSenderWalletId(1);
        requestModel.setReceiverWalletId(2);
        requestModel.setReceiverName(receiverName);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername(receiverName)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> interTransactionService.transact(requestModel));
    }

}
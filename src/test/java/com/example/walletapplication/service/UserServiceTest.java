package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testDeleteUserWhenUserExists() throws UserNotFoundException {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        String result = userService.delete(1);

        assertEquals("User deleted successfully.", result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void tesDeleteShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        String username = "testUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(1));
        verify(userRepository, never()).delete(any(User.class));
    }


    @Test
    public void testAddWalletShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        String username = "testUser";
        int userId = 1;

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addWallet(userId));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testAddWalletShouldThrowUserNotFoundExceptionWhenUserIdMismatch() {
        String username = "testUser";
        int userId = 1;
        int mismatchedUserId = 2;
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(UserNotFoundException.class, () -> userService.addWallet(mismatchedUserId));
        verify(userRepository, never()).save(any(User.class));
    }
}

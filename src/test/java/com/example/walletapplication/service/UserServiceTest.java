package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password", null);
    }

    @Test
    void testRegister_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.register("testuser", "password");

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_InvalidUsernameAndPassword() {
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.register("", "password");
        });

        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.register("testuser", "");
        });

        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.register(null, "password");
        });

        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.register("testuser", null);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDelete_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String result = userService.delete(1L);

        assertEquals("User deleted successfully.", result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void testDelete_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.delete(1L);
        });

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("testuser");
        });

        verify(userRepository, times(1)).findByUsername(anyString());
    }
}
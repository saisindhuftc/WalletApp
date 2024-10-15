package com.example.walletapplication.repository;

import com.example.walletapplication.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    @Test
    public void testFindByUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        User expectedUser = new User("sai", "password456");
        when(userRepository.findByUsername("sai")).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUserOptional = userRepository.findByUsername("sai");
        assertTrue(actualUserOptional.isPresent());

        User actualUser = actualUserOptional.get();
        assertEquals(expectedUser.getUserName(), actualUser.getUserName());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    public void testFindByUsernameNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        Optional<User> actualUserOptional = userRepository.findByUsername("nonExistentUser");
        assertFalse(actualUserOptional.isPresent());
    }
}
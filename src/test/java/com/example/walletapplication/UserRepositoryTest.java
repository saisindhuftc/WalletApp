package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    @Test
    public void testFindByUsername() {
        UserRepository userRepository = mock(UserRepository.class);

        User expectedUser = new User("sai", "password456");

        when(userRepository.findByUsername("sai")).thenReturn(expectedUser);

        User actualUser = userRepository.findByUsername("sai");

        assertNotNull(actualUser);

        assertSame(expectedUser, actualUser);

        verify(userRepository, times(1)).findByUsername("sai");
    }

    @Test
    public void testFindByUsernameNotFound() {
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        User actualUser = userRepository.findByUsername("nonExistentUser");

        assertNull(actualUser);
    }
}

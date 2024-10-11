package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserAlreadyExistsException;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    public void testRegisterUser() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);
        String username = "sravani";
        String password = "password300";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword300");

        User newUser = userService.registerUser(username, password);

        assertNotNull(newUser);
        assertEquals(username, newUser.getUsername());
        assertEquals("encodedPassword300", newUser.getPassword());
        verify(userRepository).save(newUser);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        String username = "nas";
        String password = "password789";

        when(userRepository.findByUsername(username)).thenReturn(new User(username, "encodedPassword"));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(username, password);
        });

        assertEquals("Username already exists", exception.getMessage());
    }


    @Test
    public void testGetUserByUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);
        String username = "pooji";
        User user = new User(username, "password200");

        when(userRepository.findByUsername(username)).thenReturn(user);

        User retrievedUser = userService.getUserByUsername(username);

        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
    }

    @Test
    public void testGetUserByUsernameNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = mock(UserService.class);
        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        User user = userService.getUserByUsername(username);

        assertNull(user);
    }
}

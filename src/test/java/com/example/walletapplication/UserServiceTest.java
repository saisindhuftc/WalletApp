package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidCredentialsException;
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
    public void testLoginUserSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        String username = "Sai";
        String password = "password456";
        User user = new User(username, "encodedPassword456");

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        User loggedInUser = userService.loginUser(username, password);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
    }

    @Test
    public void testLoginUserInvalidCredentials() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        String username = "nas";
        String password = "wrongpassword";
        User user = new User(username, "encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.loginUser(username, password);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    public void testLoginUserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        String username = "nonexistentuser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(null);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.loginUser(username, password);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }


    @Test
    public void testGetUserByUserId() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;
        User user = new User("pooji", "password200");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        User retrievedUser = userService.getUserByUserId(userId);

        assertNotNull(retrievedUser);
    }

    @Test
    public void testGetUserByUserIdNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = mock(UserService.class);
        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        User user = userService.getUserByUserId(100L);

        assertNull(user);
    }
}
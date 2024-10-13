package com.example.walletapplication;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
        verify(userRepository).save(newUser);
        verify(passwordEncoder).encode(password);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        String username = "nas";
        String password = "password789";

        when(userRepository.findByUsername(username)).thenReturn(new User(username, "encodedPassword"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(username, password);
        });

        assertEquals("Username already exists", exception.getMessage());
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
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());
        User user = userService.getUserByUserId(userId);

        assertNull(user);
    }

    @Test
    @Transactional
    public void testDeposit() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;

        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(user.deposit(100.0)).thenReturn(100.0);

        Double newBalance = userService.deposit(userId, 100.0);

        assertEquals(100.0, newBalance);
        verify(user).deposit(100.0);
    }

    @Test
    @Transactional
    public void testWithdraw() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;

        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(user.withdraw(50.0)).thenReturn(50.0);

        Double newBalance = userService.withdraw(userId, 50.0);

        assertEquals(50.0, newBalance);
        verify(user).withdraw(50.0);
    }

    @Test
    @Transactional
    public void testWithdrawInsufficientBalance() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;
        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        doThrow(new InsufficientBalanceException("Insufficient balance")).when(user).withdraw(150.0);
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            userService.withdraw(userId, 150.0);
        });

        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    public void testUserNotFoundForDeposit() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deposit(userId, 100.0);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUserNotFoundForWithdraw() {
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.withdraw(userId, 50.0);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
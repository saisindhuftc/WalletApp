package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.exception.UserAlreadyExistsException;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        String username = "testUser";
        String password = "testPassword";

        User newUser = new User(username, password, null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.registerUser(username, password, null);

        verify(userRepository, times(1)).save(any(User.class));
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.save(any(User.class))).thenThrow(new UserAlreadyExistsException("User already exists"));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(username, password, null);
        });

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void testRegisterUserInternalServerError() {
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(username, password, null);
        });

        assertEquals("Internal server error", exception.getMessage());
    }

    @Test
    void testRegisterUserInvalidUsername() {
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.registerUser("", "password", null);
        });
    }

    @Test
    void testRegisterUserInvalidPassword() {
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.registerUser("testUser", "", null);
        });
    }

    @Test
    void testRegisterUserInvalidUsernameAndPassword() {
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.registerUser("", "", null);
        });
    }

    @Test
    void testGetUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testGetUserDetailsUnauthorized() {
        when(userRepository.findById(anyLong())).thenThrow(new UnAuthorisedUserException("User not authorized"));

        UnAuthorisedUserException exception = assertThrows(UnAuthorisedUserException.class, () -> {
            userService.getUserById(20L);
        });

        assertEquals("User not authorized", exception.getMessage());
    }

    @Test
    void testGetUserDetailsInternalServerError() {
        when(userRepository.findById(anyLong())).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(924566332L);
        });

        assertEquals("Internal server error", exception.getMessage());
    }
}

package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.requestDTO.UserRequestDTO;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerSuccess() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testuser", "password123");
        User mockUser = new User("testUser", "testPassword", null);
        when(userService.register("testuser", "password123")).thenReturn(mockUser);

        ResponseEntity<?> response = userController.register(userRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).register("testuser", "password123");
    }

    @Test
    public void registerInvalidUsernameAndPassword() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testuser", "password123");
        when(userService.register("testuser", "password123"))
                .thenThrow(new InvalidUsernameAndPasswordException("Invalid credentials"));

        ResponseEntity<?> response = userController.register(userRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(userService, times(1)).register("testuser", "password123");
    }

    @Test
    public void deleteUserSuccess() {
        Long userId = 1L;
        String expectedResponse = "User deleted successfully";
        when(userService.delete(userId)).thenReturn(expectedResponse);

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void deleteUserUnauthorized() {
        Long userId = 1L;
        when(userService.delete(userId)).thenThrow(new UnAuthorisedUserException("User not authorized"));

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not authorized", response.getBody());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void getUserDetailsSuccess() {

        String username = "testuser";
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        ResponseEntity<?> response = userController.getUserDetails(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserDetails, response.getBody());
        verify(userService, times(1)).loadUserByUsername(username);
    }

    @Test
    public void getUserDetailsNotFound() {
        String username = "unknownuser";
        when(userService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userController.getUserDetails(username);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).loadUserByUsername(username);
    }
}
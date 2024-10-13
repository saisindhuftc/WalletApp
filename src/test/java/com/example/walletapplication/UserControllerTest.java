package com.example.walletapplication;

import com.example.walletapplication.controller.UserController;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserAlreadyExistsException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        User user = new User("sai", "password456");
        when(userService.registerUser("sai", "password456")).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .param("username", "sai")
                        .param("password", "password456")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        verify(userService, times(1)).registerUser("sai", "password456");
    }

    @Test
    public void testRegisterUserUsernameAlreadyExists() throws Exception {
        doThrow(new UserAlreadyExistsException("Username already exists"))
                .when(userService).registerUser("nas", "password789");

        mockMvc.perform(post("/api/users/register")
                        .param("username", "nas")
                        .param("password", "password789")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isConflict())
                .andExpect(content().string("Username already exists"));
        verify(userService, times(1)).registerUser("nas", "password789");
    }

    @Test
    public void testDepositSuccess() throws Exception {
        when(userService.deposit(1L, 100.0)).thenReturn(300.0);

        mockMvc.perform(post("/api/users/1/deposit")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful. New balance: 300.0"));
        verify(userService, times(1)).deposit(1L, 100.0);
    }

    @Test
    public void testDepositUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found"))
                .when(userService).deposit(5L, 100.0);

        mockMvc.perform(post("/api/users/5/deposit")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, times(1)).deposit(5L, 100.0);
    }

    @Test
    public void testDepositInvalidAmount() throws Exception {
        doThrow(new InvalidAmountException("Invalid amount"))
                .when(userService).deposit(1L, -100.0);

        mockMvc.perform(post("/api/users/1/deposit")
                        .param("amount", "-100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Invalid amount"));

        verify(userService, times(1)).deposit(1L, -100.0);
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        when(userService.withdraw(2L, 50.0)).thenReturn(150.0);

        mockMvc.perform(post("/api/users/2/withdraw")
                        .param("amount", "50.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful. New balance: 150.0"));

        verify(userService, times(1)).withdraw(2L, 50.0);
    }

    @Test
    public void testWithdrawUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found"))
                .when(userService).withdraw(99L, 50.0);

        mockMvc.perform(post("/api/users/99/withdraw")
                        .param("amount", "50.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, times(1)).withdraw(99L, 50.0);
    }

    @Test
    public void testWithdrawInvalidAmount() throws Exception {
        doThrow(new InvalidAmountException("Invalid amount"))
                .when(userService).withdraw(1L, -50.0);

        mockMvc.perform(post("/api/users/1/withdraw")
                        .param("amount", "-50.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Invalid amount"));

        verify(userService, times(1)).withdraw(1L, -50.0);
    }

    @Test
    public void testWithdrawInsufficientBalance() throws Exception {
        doThrow(new InsufficientBalanceException("Insufficient balance"))
                .when(userService).withdraw(3L, 5000.0);

        mockMvc.perform(post("/api/users/3/withdraw")
                        .param("amount", "5000.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient balance"));

        verify(userService, times(1)).withdraw(3L, 5000.0);
    }

    @Test
    public void testGetUserByUserIdSuccess() throws Exception {
        User user = new User("pooji", "password200");
        when(userService.getUserByUserId(4L)).thenReturn(user);

        mockMvc.perform(get("/api/users/4"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserByUserId(4L);
    }

    @Test
    public void testGetUserByUserIdNotFound() throws Exception {
        when(userService.getUserByUserId(99L)).thenReturn(null);

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUserId(99L);
    }
}
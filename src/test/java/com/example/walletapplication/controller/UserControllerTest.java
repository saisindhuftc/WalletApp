package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testDeleteUserSuccess() throws Exception {
        when(userService.delete(anyInt())).thenReturn("User deleted successfully.");

        mockMvc.perform(delete("/api/v1/users/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(anyInt());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testDeleteUserNotFound() throws Exception {
        when(userService.delete(anyInt())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(delete("/api/v1/users/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, times(1)).delete(anyInt());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testAddWalletSuccess() throws Exception {
        User mockUser = new User();
        when(userService.addWallet(anyInt())).thenReturn(mockUser);

        mockMvc.perform(put("/api/v1/users/1/wallet/1/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(userService, times(1)).addWallet(anyInt());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testAddWalletUserNotFound() throws Exception {
        when(userService.addWallet(anyInt())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/api/v1/users/1/wallet/1/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).addWallet(anyInt());
    }
}
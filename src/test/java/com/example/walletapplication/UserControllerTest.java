package com.example.walletapplication;

import com.example.walletapplication.Controller.UserController;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserAlreadyExistsException;
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
        User user = new User("Sai", "password456");
        when(userService.registerUser("Sai", "password456")).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .param("username", "Sai")
                        .param("password", "password456")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Sai"));
    }

    @Test
    public void testRegisterUserUsernameAlreadyExists() throws Exception {
        doThrow(new UserAlreadyExistsException("Username already exists"))
                .when(userService).registerUser("nas", "password789");

        mockMvc.perform(post("/api/users/register")
                        .param("username", "nas")
                        .param("password", "password789")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    public void testGetUserByUsernameSuccess() throws Exception {
        User user = new User("pooji", "password200");
        when(userService.getUserByUsername("pooji")).thenReturn(user);

        mockMvc.perform(get("/api/users/pooji"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("pooji"));
    }

    @Test
    public void testGetUserByUsername_NotFound() throws Exception {
        when(userService.getUserByUsername("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/api/users/nonexistent"))
                .andExpect(status().isNotFound());
    }
}

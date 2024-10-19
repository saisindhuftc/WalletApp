package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        User user = new User("testuser", "password", null);

        when(userService.registerUser(any(String.class), any(String.class), any())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("testuser"));

        verify(userService, times(1)).registerUser("testuser", "password", null);
    }

    @Test
    public void testRegisterUserInvalidUsername() throws Exception {
        doThrow(new InvalidUsernameAndPasswordException("Username and password cannot be empty"))
                .when(userService).registerUser(any(String.class), any(String.class), any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username and password cannot be empty"))
                .andExpect(jsonPath("$.status").value("Invalid credentials"));

        verify(userService, times(1)).registerUser("", "password", null);
    }

    @Test
    public void testRegisterUserInvalidPassword() throws Exception {
        doThrow(new InvalidUsernameAndPasswordException("Username and password cannot be empty"))
                .when(userService).registerUser(any(String.class), any(String.class), any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username and password cannot be empty"))
                .andExpect(jsonPath("$.status").value("Invalid credentials"));

        verify(userService, times(1)).registerUser("testuser", "", null);
    }

    @Test
    public void testRegisterUserInvalidUsernameAndPassword() throws Exception {
        doThrow(new InvalidUsernameAndPasswordException("Username and password cannot be empty"))
                .when(userService).registerUser(any(String.class), any(String.class), any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username and password cannot be empty"))
                .andExpect(jsonPath("$.status").value("Invalid credentials"));

        verify(userService, times(1)).registerUser("", "", null);
    }

    @Test
    public void testRegisterUserInternalServerError() throws Exception {
        doThrow(new RuntimeException("Internal server error")).when(userService).registerUser(any(String.class), any(String.class), any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.status").value("Internal server error"));

        verify(userService, times(1)).registerUser("testuser", "password", null);
    }

    @Test
    public void testRegisterUserAlreadyExists() throws Exception {
        doThrow(new UserAlreadyExistsException("User already exists")).when(userService).registerUser(any(String.class), any(String.class), any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists"))
                .andExpect(jsonPath("$.status").value("User already exists"));

        verify(userService, times(1)).registerUser("testuser", "password", null);
    }

    @Test
    public void testGetUserDetails() throws Exception {
        User user = new User();
        user.setId(1L);

        when(userService.getUserById(anyLong())).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1L));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetUserDetailsUnauthorized() throws Exception {
        doThrow(new UnAuthorisedUserException("User not authorized")).when(userService).getUserById(anyLong());

        mockMvc.perform(get("/users/20"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized"))
                .andExpect(jsonPath("$.status").value("User not authorized"));

        verify(userService, times(1)).getUserById(20L);
    }

    @Test
    public void testGetUserDetailsInternalServerError() throws Exception {
        doThrow(new RuntimeException("Internal server error")).when(userService).getUserById(anyLong());

        mockMvc.perform(get("/users/924566332"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.status").value("Internal server error"));

        verify(userService, times(1)).getUserById(924566332L);
    }

}
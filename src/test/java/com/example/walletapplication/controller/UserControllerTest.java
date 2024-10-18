package com.example.walletapplication.controller;

import com.example.walletapplication.controller.UserController;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.requestDTO.UserRequestDTO;
import com.example.walletapplication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for successful registration
    @Test
    public void testRegisterWhenSuccessful() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testUser", "testPassword");
        User mockUser = new User("testUser", "testPassword", CurrencyType.INR);

        when(userService.register("testUser", "testPassword")).thenReturn(mockUser);

        MvcResult mvcResult = mockMvc.perform(post("/users")  // Updated path
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk()) // Verify status 200 OK
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(mockUser);
        assertEquals(expectedResponse, response);

        verify(userService, times(1)).register("testUser", "testPassword");
    }


    // Test when username and password are invalid
    @Test
    public void testRegisterWhenInvalidUsernameOrPassword() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO("invalidUser", "short");

        when(userService.register("invalidUser", "short"))
                .thenThrow(new InvalidUsernameAndPasswordException("Invalid username or password"));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest()) // Verify status 400 Bad Request
                .andExpect(result -> assertEquals("Invalid username or password", result.getResponse().getContentAsString()));

        verify(userService, times(1)).register("invalidUser", "short");
    }

    // Test for general server error during registration
    @Test
    public void testRegisterWhenServerError() throws Exception {
        // Prepare input data
        UserRequestDTO userRequestDTO = new UserRequestDTO("testUser", "testPassword");

        when(userService.register("testUser", "testPassword"))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isInternalServerError()) // Verify status 500 Internal Server Error
                .andExpect(result -> assertEquals("An error occurred: Unexpected error", result.getResponse().getContentAsString()));

        verify(userService, times(1)).register("testUser", "testPassword");
    }

    @Test
    public void testDeleteUserWhenSuccessful() throws Exception {
        Long userId = 1L;
        String expectedResponse = "User successfully deleted";

        when(userService.delete(userId)).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/delete", userId))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedResponse, response);

        verify(userService, times(1)).delete(userId);
    }

    // Test for unauthorized deletion
    @Test
    public void testDeleteUserWhenUnauthorized() throws Exception {
        Long userId = 1L;

        doThrow(new UnAuthorisedUserException("User not authorized")).when(userService).delete(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/delete", userId))
                .andExpect(status().isForbidden()) // Verify status 403 Forbidden
                .andExpect(result -> assertEquals("User not authorized", result.getResponse().getContentAsString()));

        verify(userService, times(1)).delete(userId);
    }

    // Test for internal server error during deletion
    @Test
    public void testDeleteUserWhenServerError() throws Exception {
        Long userId = 1L;
        doThrow(new RuntimeException("Unexpected error")).when(userService).delete(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/delete", userId))
                .andExpect(status().isInternalServerError()) // Verify status 500 Internal Server Error
                .andExpect(result -> assertEquals("An error occurred: Unexpected error", result.getResponse().getContentAsString()));

        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testGetUserDetailsWhenSuccessful() throws Exception {
        String username = "testUser";
        UserDetails mockUserDetails = mock(UserDetails.class);

        when(userService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", username))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(mockUserDetails);

        assertEquals(expectedResponse, response);

        verify(userService, times(1)).loadUserByUsername(username);
    }

    // Test when the user is not found
    @Test
    public void testGetUserDetailsWhenUserNotFound() throws Exception {
        String username = "unknownUser";

        when(userService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", username))
                .andExpect(status().isBadRequest()) // Verify status 400 Bad Request
                .andExpect(result -> assertEquals("User not found", result.getResponse().getContentAsString()));

        verify(userService, times(1)).loadUserByUsername(username);
    }

    // Test for internal server error during user retrieval
    @Test
    public void testGetUserDetailsWhenServerError() throws Exception {
        String username = "testUser";

        when(userService.loadUserByUsername(username)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", username))
                .andExpect(status().isInternalServerError()) // Verify status 500 Internal Server Error
                .andExpect(result -> assertEquals("An error occurred: Unexpected error", result.getResponse().getContentAsString()));

        verify(userService, times(1)).loadUserByUsername(username);
    }

}

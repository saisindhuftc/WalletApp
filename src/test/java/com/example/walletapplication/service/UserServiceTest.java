package com.example.walletapplication.service;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testUserRegistration() {
        UserRepository userRepository = mock(UserRepository.class);
        assertDoesNotThrow(() -> {
            new UserService(userRepository);
        });
    }

    @Test
    void testRegisterUserWhenUserNameIsEmpty() {
        UserRepository userRepository = mock(UserRepository.class);

        UserService userService = new UserService(userRepository);
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.registerUser("","Password",null);
        });
    }

    @Test
    void testRegisterUserWhenPasswordIsEmpty() {
        UserRepository userRepository = mock(UserRepository.class);

        UserService userService = new UserService(userRepository);
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.registerUser("username","",null);
        });
    }

    @Test
    void testRegisterUserWhenPasswordAndUserNameIsEmpty() {
        UserRepository userRepository = mock(UserRepository.class);

        UserService userService = new UserService(userRepository);
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            userService.registerUser("","",null);
        });
    }

    @Test
    void testRegisterUser() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        String username = "testUser";
        String password = "testPassword";

        User savedUser = new User(username,password,null);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        User result = userService.registerUser(username, password,null);

        verify(userRepository,times(1)).save(any(User.class));
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
    }
}
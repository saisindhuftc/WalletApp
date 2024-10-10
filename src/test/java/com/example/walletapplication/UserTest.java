package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("testUser", "testPassword");
        assertNotNull(user);
    }

    @Test
    public void testUserAfterCreation() {
        Wallet wallet = new Wallet();
        User user = new User("testUser", "testPassword");
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertNotNull(user.getWallet());
    }

    @Test
    public void testValidId() {
        User user = new User("username", "password");
        assertNull(user.getId());
    }

    @Test
    public void testInValidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "password");
        });
    }

    @Test
    public void testValidUsername() {
        User user = new User("validUser", "validPassword");
        assertEquals("validUser", user.getUsername());
    }

    @Test
    public void testInValidUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "validPassword");
        });
    }

    @Test
    public void testValidPassword() {
        User user = new User("validUser", "validPassword");
        assertEquals("validPassword", user.getPassword());
    }

    @Test
    public void testUserWallet() {
        User user = new User("testUser", "testPassword");
        assertNotNull(user.getWallet());
    }


    @Test
    public void testTwoUsers() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        assertNotEquals(user1, user2);
    }

    @Test
    public void testUserUpdateUsernameAndPassword() {
        User user = new User("oldUser", "oldPassword");
        user = new User("newUser", "newPassword");
        assertEquals("newUser", user.getUsername());
        assertEquals("newPassword", user.getPassword());
    }
}
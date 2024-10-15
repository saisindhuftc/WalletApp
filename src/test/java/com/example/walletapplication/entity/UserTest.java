package com.example.walletapplication.entity;

import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("sai", "password456");
        assertNotNull(user);
    }

    @Test
    public void testUserAfterCreation() {
        User user = new User("sai", "password456");
        assertEquals("sai", user.getUserName());
        assertEquals("password456", user.getPassword());
        assertNotNull(user.getWallets());
    }

    @Test
    public void testValidId() {
        User user = new User("sindhu", "password123");
        assertNull(user.getUserId());
    }

    @Test
    public void testInValidUserNameIsNull() {
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User(null, "password123");
        });
    }

    @Test
    public void testValidUsername() {
        User user = new User("lahari", "password100");
        assertEquals("lahari", user.getUserName());
    }

    @Test
    public void testInValidUsername() {
        assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User("", "password100");
        });
    }

    @Test
    public void testValidPassword() {
        User user = new User("nas", "password789");
        assertEquals("password789", user.getPassword());
    }

    @Test
    public void testUserWallet() {
        User user = new User("pooji", "password200");
        assertNotNull(user.getWallets());
    }

    @Test
    public void testTwoUsers() {
        User user1 = new User("sai", "password456");
        User user2 = new User("sindhu", "password123");
        assertNotEquals(user1, user2);
    }

    @Test
    public void testUserUpdateUsernameAndPassword() {
        User user = new User("nas", "Password789");
        user = new User("nasira", "password254");
        assertEquals("nasira", user.getUserName());
        assertEquals("password254", user.getPassword());
    }
}
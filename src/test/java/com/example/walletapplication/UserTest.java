package com.example.walletapplication;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreationWithValidUsernameAndPassword() {
        User user = new User("sai", "password456");
        assertNotNull(user);
    }

    @Test
    void testUserCreationWithNullUsername() {
        Exception exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User(null, "validpassword");
        });
        assertEquals("Invalid user username and password must not be null or empty", exception.getMessage());
    }

    @Test
    void testUserCreationWithEmptyUsername() {
        Exception exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User("", "validPassword");
        });
        assertEquals("Invalid user username and password must not be null or empty", exception.getMessage());
    }

    @Test
    void testUserCreationWithNullPassword() {
        Exception exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User("validuser", null);
        });
        assertEquals("Invalid user username and password must not be null or empty", exception.getMessage());
    }

    @Test
    void testUserCreationWithEmptyPassword() {
        Exception exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User("validUser", "");
        });
        assertEquals("Invalid user username and password must not be null or empty", exception.getMessage());
    }

    @Test
    void testDepositInWallet() {
        User user = new User("sindhu", "password123");
        Double newBalance = user.deposit(100.0);
        assertEquals(100.0, newBalance);
    }

    @Test
    void testWithdrawFromWallet() {
        User user = new User("laharir", "password100");
        user.deposit(200.0);
        Double newBalance = user.withdraw(100.0);
        assertEquals(100.0, newBalance);
    }

    @Test
    void testWithdrawInsufficientBalance() {
        User user = new User("sai", "password456");
        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            user.withdraw(100.0);
        });
        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    public void testTwoUsers() {
        User user1 = new User("sai", "password456");
        User user2 = new User("sindhu", "password123");
        assertNotEquals(user1, user2);
    }
}

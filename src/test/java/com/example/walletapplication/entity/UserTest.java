package com.example.walletapplication.entity;

import com.example.walletapplication.enums.Country;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("sai", "password456",Country.USA);
        assertNotNull(user);
    }

    @Test
    public void testUserAfterCreation() {
        User user = new User("sai", "password456",Country.USA);
        assertEquals("sai", user.getUsername());
        assertEquals("password456", user.getPassword());
        assertNotNull(user.getWallets());
    }

    @Test
    public void testValidId() {
        User user = new User("sindhu", "password123",Country.INDIA);
        assertNull(user.getUserId());
    }


    @Test
    public void testValidUsername() {
        User user = new User("lahari", "password100",Country.EUROPE);
        assertEquals("lahari", user.getUsername());
    }


    @Test
    public void testValidPassword() {
        User user = new User("nas", "password789",Country.EUROPE);
        assertEquals("password789", user.getPassword());
    }

    @Test
    public void testUserWallet() {
        User user = new User("pooji", "password200", Country.INDIA);
        assertNotNull(user.getWallets());
    }

    @Test
    public void testTwoUsers() {
        User user1 = new User("sai", "password456",Country.USA);
        User user2 = new User("sindhu", "password123",Country.INDIA);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testUserUpdateUsernameAndPassword() {
        User user = new User("nasira", "password254", Country.EUROPE);
        assertEquals("nasira", user.getUsername());
        assertEquals("password254", user.getPassword());
    }

    @Test
    public void testAddMultipleWallets() {
        User user = new User("mike", "password987", Country.INDIA);
        Wallet wallet1 = new Wallet(Country.USA);
        Wallet wallet2 = new Wallet(Country.EUROPE);
        user.getWallets().add(wallet1);
        user.getWallets().add(wallet2);
        assertEquals(3, user.getWallets().size());
    }

    @Test
    public void testPasswordWriteOnly() {
        User user = new User("writeOnlyUser", "password789", Country.INDIA);
        assertEquals("password789", user.getPassword());
    }
}
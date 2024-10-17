package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InvalidUsernameAndPasswordException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void testUserInitialization() {
        String username = "testUser";
        String password = "securePassword";
        CurrencyType currencyType = CurrencyType.INR;

        User user = new User(username, password, currencyType);

        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertNotNull(user.getWallet());
        assertEquals(currencyType, user.getWallet().getCurrency());
    }

    @Test
    public void testUserWithNullUsername() {
        String password = "securePassword";
        CurrencyType currencyType = CurrencyType.INR;

        InvalidUsernameAndPasswordException exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User(null, password, currencyType);
        });
        assertEquals("Username and password cannot be null or blank", exception.getMessage());
    }

    @Test
    void testUserWithBlankUsername() {
        String password = "securePassword";
        CurrencyType currencyType = CurrencyType.INR;

        InvalidUsernameAndPasswordException exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User("  ", password, currencyType);
        });
        assertEquals("Username and password cannot be null or blank", exception.getMessage());
    }

    @Test
    void testUserWithNullPassword() {
        String username = "testUser";
        CurrencyType currencyType = CurrencyType.INR;

        InvalidUsernameAndPasswordException exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User(username, null, currencyType);
        });
        assertEquals("Username and password cannot be null or blank", exception.getMessage());
    }

    @Test
    void testUserWithBlankPassword() {
        String username = "testUser";
        CurrencyType currencyType = CurrencyType.INR;

        InvalidUsernameAndPasswordException exception = assertThrows(InvalidUsernameAndPasswordException.class, () -> {
            new User(username, "  ", currencyType);
        });
        assertEquals("Username and password cannot be null or blank", exception.getMessage());
    }

    @Test
    void testUserWithNullCurrencyType() {
        String username = "testUser";
        String password = "securePassword";

        User user = new User(username, password, null);

        assertNotNull(user.getWallet());
        assertEquals(CurrencyType.INR, user.getWallet().getCurrency());
    }

    @Test
    public void testUserUpdateNameAndPassword() {
        User user = new User("username", "password", CurrencyType.USD);

        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testTwoUser() {
        String userName1 = "sai";
        String password1 = "Password123";
        CurrencyType currencyType = CurrencyType.USD;
        String userName2 = "sindhu";
        String password2 = "password456";
        CurrencyType currencyType1 = CurrencyType.USD;
        User user = new User(userName1, password1, currencyType);
        User user2 = new User(userName2, password2, currencyType1);

        assertNotEquals(user, user2);
    }
}

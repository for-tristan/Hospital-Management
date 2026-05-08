package tests;

import dao.UserDAO;
import database.DatabaseConnection;
import models.User;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;

public class test {
    
    private UserDAO userDAO = new UserDAO();

    @Test
    public void testDatabaseConnection() {
        try {
            // This forces the real error to show up in JUnit if it fails!
            Connection conn = DatabaseConnection.getConnection();
            
            if (conn == null) {
                fail("CONNECTION IS NULL! Check your password in DatabaseConnection.java");
            }
            if (conn.isClosed()) {
                fail("CONNECTION IS CLOSED!");
            }
            
        } catch (Exception e) {
            fail("DATABASE ERROR: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterAndLogin() {
        String uniqueUsername = "junit_user_" + System.currentTimeMillis();
        String uniqueEmail = "junit_" + System.currentTimeMillis() + "@test.com"; // Made email unique too
        
        // 1. Test Registration
        User newUser = new User(uniqueUsername, "password123", "JUnit Test", uniqueEmail, "555", "patient");
        boolean registerResult = userDAO.registerUser(newUser);
        
        assertTrue("Registration failed! Check the Output tab for SQL errors.", registerResult);
        assertTrue("User should get an ID from database", newUser.getUserId() > 0);
        
        // 2. Test Successful Login
        User loggedInUser = userDAO.loginUser(uniqueUsername, "password123");
        assertNotNull("Login should find the user", loggedInUser);
        assertEquals("Full name should match", "JUnit Test", loggedInUser.getFullName());
        
        // 3. Test Failed Login (Wrong Password)
        User wrongPasswordUser = userDAO.loginUser(uniqueUsername, "wrongpassword");
        assertNull("Login with wrong password should return null", wrongPasswordUser);
    }
    
    @Test
    public void testUsernameExists() {
        String uniqueUsername = "junit_check_" + System.currentTimeMillis();
        String uniqueEmail = "check_" + System.currentTimeMillis() + "@test.com";
        
        // Should NOT exist before registering
        assertFalse("Username should not exist yet", userDAO.usernameExists(uniqueUsername));
        
        // Register it
        User newUser = new User(uniqueUsername, "pass", "Test", uniqueEmail, "555", "patient");
        userDAO.registerUser(newUser);
        
        // SHOULD exist now
        assertTrue("Username should exist now", userDAO.usernameExists(uniqueUsername));
    }
}
package tests;

import dao.UserDAO;
import dao.AppointmentDAO;
import dao.DoctorDAO;
import models.User;
import models.Appointment;
import java.sql.Date;

public class RunAutomatedTests {
    
    // Counters
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("  HOSPITAL SYSTEM DATABASE TESTS");
        System.out.println("=======================================\n");
        
        UserDAO userDAO = new UserDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        
        // Generate a unique random username so we never clash with existing data
        String uniqueUser = "auto_test_" + System.currentTimeMillis();
        
        // ---------------------------------------------------------
        // TEST 1: Register a new user
        // ---------------------------------------------------------
        User newUser = new User(uniqueUser, "testpass", "Auto Test User", "auto@test.com", "123", "patient");
        boolean isRegistered = userDAO.registerUser(newUser);
        
        if (assertTest("Test 1: User Registration", isRegistered == true)) {
            assertTest("Test 1b: User Got ID", newUser.getUserId() > 0);
        }
        
        // ---------------------------------------------------------
        // TEST 2: Check if username exists
        // ---------------------------------------------------------
        boolean existsNow = userDAO.usernameExists(uniqueUser);
        assertTest("Test 2: Username Exists Check", existsNow == true);
        
        // ---------------------------------------------------------
        // TEST 3: Login with correct password
        // ---------------------------------------------------------
        User loggedIn = userDAO.loginUser(uniqueUser, "testpass");
        assertTest("Test 3: Successful Login", loggedIn != null);
        if (loggedIn != null) {
            assertTest("Test 3b: Correct Name Loaded", loggedIn.getFullName().equals("Auto Test User"));
        }
        
        // ---------------------------------------------------------
        // TEST 4: Login with WRONG password
        // ---------------------------------------------------------
        User wrongLogin = userDAO.loginUser(uniqueUser, "wrongpassword");
        assertTest("Test 4: Failed Login (Wrong Pass)", wrongLogin == null);
        
        // ---------------------------------------------------------
        // TEST 5: Get all doctors
        // ---------------------------------------------------------
        int doctorCount = doctorDAO.getAllDoctors().size();
        assertTest("Test 5: Get All Doctors (>0)", doctorCount > 0);
        
        // ---------------------------------------------------------
        // TEST 6: Book an appointment
        // ---------------------------------------------------------
        if (loggedIn != null && doctorCount > 0) {
            int firstDocId = doctorDAO.getAllDoctors().get(0).getDoctorId();
            Date futureDate = Date.valueOf("2025-12-25"); // Far future date
            
            Appointment apt = new Appointment(loggedIn.getUserId(), firstDocId, futureDate, "10:00", "Automated Test");
            boolean isBooked = appointmentDAO.bookAppointment(apt);
            
            if (assertTest("Test 6: Book Appointment", isBooked == true)) {
                assertTest("Test 6b: Appointment Got ID", apt.getAppointmentId() > 0);
            }
            
            // ---------------------------------------------------------
            // TEST 7: Check time slot availability
            // ---------------------------------------------------------
            boolean slotTaken = appointmentDAO.isTimeSlotAvailable(firstDocId, futureDate, "10:00");
            assertTest("Test 7: Time Slot Taken Check", slotTaken == false);
            
            // ---------------------------------------------------------
            // TEST 8: Cancel appointment
            // ---------------------------------------------------------
            boolean isCancelled = appointmentDAO.cancelAppointment(apt.getAppointmentId());
            assertTest("Test 8: Cancel Appointment", isCancelled == true);
        }
        
        // ---------------------------------------------------------
        // FINAL RESULTS
        // ---------------------------------------------------------
        System.out.println("\n=======================================");
        System.out.println("  TESTS FINISHED");
        System.out.println("  PASSED: " + passed);
        System.out.println("  FAILED: " + failed);
        System.out.println("=======================================");
        
        if (failed == 0) {
            System.out.println("✅ ALL SYSTEMS GO! Database is perfect.");
        } else {
            System.out.println("❌ SOME TESTS FAILED. Check MySQL.");
        }
    }
    
    // Custom helper method to print pretty results
    private static boolean assertTest(String testName, boolean condition) {
        if (condition) {
            System.out.println("✅ PASS: " + testName);
            passed++;
            return true;
        } else {
            System.out.println("❌ FAIL: " + testName);
            failed++;
            return false;
        }
    }
}
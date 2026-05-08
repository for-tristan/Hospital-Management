package dao;

import database.DatabaseConnection;
import models.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE status = 'available'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Doctor doctor = mapResultSetToDoctor(rs);
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error getting doctors: " + e.getMessage());
        }
        return doctors;
    }
    
    public Doctor getDoctorById(int doctorId) {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDoctor(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting doctor: " + e.getMessage());
        }
        return null;
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization LIKE ? AND status = 'available'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + specialization + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapResultSetToDoctor(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching doctors: " + e.getMessage());
        }
        return doctors;
    }
    
    public List<String> getAllSpecializations() {
        List<String> specializations = new ArrayList<>();
        String sql = "SELECT DISTINCT specialization FROM doctors WHERE status = 'available' ORDER BY specialization";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                specializations.add(rs.getString("specialization"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting specializations: " + e.getMessage());
        }
        return specializations;
    }
    
    public boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (full_name, specialization, phone, email, experience_years, available_days, available_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, doctor.getFullName());
            pstmt.setString(2, doctor.getSpecialization());
            pstmt.setString(3, doctor.getPhone());
            pstmt.setString(4, doctor.getEmail());
            pstmt.setInt(5, doctor.getExperienceYears());
            pstmt.setString(6, doctor.getAvailableDays());
            pstmt.setString(7, doctor.getAvailableTime());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        doctor.setDoctorId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
        }
        return false;
    }
    
    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setUserId(rs.getInt("user_id"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setEmail(rs.getString("email"));
        doctor.setExperienceYears(rs.getInt("experience_years"));
        doctor.setAvailableDays(rs.getString("available_days"));
        doctor.setAvailableTime(rs.getString("available_time"));
        doctor.setStatus(rs.getString("status"));
        return doctor;
    }
}
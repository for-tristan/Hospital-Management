package dao;

import database.DatabaseConnection;
import models.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    
    public boolean bookAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setDate(3, appointment.getAppointmentDate());
            pstmt.setString(4, appointment.getAppointmentTime());
            pstmt.setString(5, appointment.getReason());
            pstmt.setString(6, appointment.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        appointment.setAppointmentId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error booking appointment: " + e.getMessage());
        }
        return false;
    }
    
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, d.full_name as doctor_name, d.specialization " +
                     "FROM appointments a " +
                     "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "WHERE a.patient_id = ? " +
                     "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting patient appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name as patient_name " +
                     "FROM appointments a " +
                     "JOIN users u ON a.patient_id = u.user_id " +
                     "WHERE a.doctor_id = ? " +
                     "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting doctor appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name as patient_name, d.full_name as doctor_name, d.specialization " +
                     "FROM appointments a " +
                     "JOIN users u ON a.patient_id = u.user_id " +
                     "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
        }
        return false;
    }
    
    public boolean cancelAppointment(int appointmentId) {
        return updateAppointmentStatus(appointmentId, "cancelled");
    }
    
    public boolean isTimeSlotAvailable(int doctorId, Date appointmentDate, String appointmentTime) {
        String sql = "SELECT appointment_id FROM appointments " +
                     "WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status != 'cancelled'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, appointmentDate);
            pstmt.setString(3, appointmentTime);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking time slot: " + e.getMessage());
        }
        return false;
    }
    
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setAppointmentDate(rs.getDate("appointment_date"));
        appointment.setAppointmentTime(rs.getString("appointment_time"));
        appointment.setReason(rs.getString("reason"));
        appointment.setStatus(rs.getString("status"));
        appointment.setCreatedAt(rs.getTimestamp("created_at"));
        
        try {
            appointment.setPatientName(rs.getString("patient_name"));
        } catch (SQLException e) {}
        try {
            appointment.setDoctorName(rs.getString("doctor_name"));
        } catch (SQLException e) {}
        try {
            appointment.setSpecialization(rs.getString("specialization"));
        } catch (SQLException e) {}
        
        return appointment;
    }
}
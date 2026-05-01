-- Create Database
CREATE DATABASE IF NOT EXISTS hospital_management;
USE hospital_management;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    role VARCHAR(20) DEFAULT 'patient',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Doctors Table
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    full_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    experience_years INT,
    available_days VARCHAR(50),
    available_time VARCHAR(50),
    status VARCHAR(20) DEFAULT 'available',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Appointments Table
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time VARCHAR(10) NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'scheduled',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- Insert Sample Doctors
INSERT INTO doctors (full_name, specialization, phone, email, experience_years, available_days, available_time) VALUES
('Dr. Sarah Johnson', 'Cardiologist', '555-0101', 'sarah.johnson@hospital.com', 15, 'Mon,Wed,Fri', '09:00-17:00'),
('Dr. Michael Chen', 'Neurologist', '555-0102', 'michael.chen@hospital.com', 12, 'Tue,Thu,Sat', '10:00-18:00'),
('Dr. Emily Davis', 'Dermatologist', '555-0103', 'emily.davis@hospital.com', 8, 'Mon,Tue,Thu', '08:00-16:00'),
('Dr. James Wilson', 'Orthopedic', '555-0104', 'james.wilson@hospital.com', 20, 'Mon,Wed,Fri', '09:00-17:00'),
('Dr. Lisa Anderson', 'Pediatrician', '555-0105', 'lisa.anderson@hospital.com', 10, 'Tue,Wed,Thu,Fri', '08:00-15:00'),
('Dr. Robert Taylor', 'General Physician', '555-0106', 'robert.taylor@hospital.com', 5, 'Mon-Sat', '09:00-18:00');

-- Insert Admin User
INSERT INTO users (username, password, full_name, email, phone, role) VALUES
('admin', 'admin123', 'System Administrator', 'admin@hospital.com', '555-0000', 'admin');
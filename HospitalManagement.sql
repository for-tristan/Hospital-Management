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
('Dr. Robert Taylor', 'General Physician', '555-0106', 'robert.taylor@hospital.com', 5, 'Mon-Sat', '09:00-18:00'),
('Dr. Michael Carter', 'Cardiologist', '555-0106', 'michael.carter@hospital.com', 11, 'Mon,Tue,Wed', '09:00-16:00'),
('Dr. Sarah Mitchell', 'Dermatologist', '555-0107', 'sarah.mitchell@hospital.com', 12, 'Tue,Thu,Fri', '10:00-17:00'),
('Dr. David Lee', 'Neurologist', '555-0108', 'david.lee@hospital.com', 13, 'Mon,Wed,Fri', '08:00-14:00'),
('Dr. Emily Roberts', 'Orthopedic Surgeon', '555-0109', 'emily.roberts@hospital.com', 14, 'Mon,Tue,Thu', '11:00-18:00'),
('Dr. James Wilson', 'General Practitioner', '555-0110', 'james.wilson@hospital.com', 15, 'Wed,Thu,Fri', '07:00-13:00'),
('Dr. Olivia Harris', 'Gynecologist', '555-0111', 'olivia.harris@hospital.com', 16, 'Mon,Tue,Wed,Fri', '09:00-15:00'),
('Dr. Daniel Young', 'Psychiatrist', '555-0112', 'daniel.young@hospital.com', 17, 'Tue,Thu', '12:00-19:00'),
('Dr. Sophia King', 'ENT Specialist', '555-0113', 'sophia.king@hospital.com', 18, 'Mon,Wed,Thu', '08:00-14:00'),
('Dr. Matthew Scott', 'Ophthalmologist', '555-0114', 'matthew.scott@hospital.com', 19, 'Tue,Fri', '10:00-16:00'),
('Dr. Ava Green', 'Radiologist', '555-0115', 'ava.green@hospital.com', 20, 'Mon,Tue,Wed', '07:00-15:00'),
('Dr. Christopher Adams', 'Urologist', '555-0116', 'christopher.adams@hospital.com', 21, 'Thu,Fri', '09:00-17:00'),
('Dr. Isabella Baker', 'Oncologist', '555-0117', 'isabella.baker@hospital.com', 22, 'Mon,Wed,Fri', '08:00-14:00'),
('Dr. Andrew Nelson', 'Pulmonologist', '555-0118', 'andrew.nelson@hospital.com', 23, 'Tue,Thu', '11:00-18:00'),
('Dr. Mia Hall', 'Endocrinologist', '555-0119', 'mia.hall@hospital.com', 24, 'Mon,Tue,Fri', '09:00-15:00'),
('Dr. Joseph Allen', 'Nephrologist', '555-0120', 'joseph.allen@hospital.com', 25, 'Wed,Thu', '08:00-13:00'),
('Dr. Charlotte Wright', 'Pediatrician', '555-0121', 'charlotte.wright@hospital.com', 26, 'Mon,Tue,Wed', '10:00-17:00'),
('Dr. Benjamin Walker', 'Cardiologist', '555-0122', 'benjamin.walker@hospital.com', 27, 'Thu,Fri', '07:00-14:00'),
('Dr. Amelia Turner', 'Dermatologist', '555-0123', 'amelia.turner@hospital.com', 28, 'Mon,Wed,Fri', '09:00-16:00'),
('Dr. Ethan Phillips', 'Neurologist', '555-0124', 'ethan.phillips@hospital.com', 29, 'Tue,Thu', '08:00-15:00'),
('Dr. Harper Campbell', 'Orthopedic Surgeon', '555-0125', 'harper.campbell@hospital.com', 30, 'Mon,Tue,Thu', '11:00-19:00'),
('Dr. Alexander Parker', 'General Practitioner', '555-0126', 'alexander.parker@hospital.com', 31, 'Wed,Fri', '07:00-13:00'),
('Dr. Evelyn Evans', 'Gynecologist', '555-0127', 'evelyn.evans@hospital.com', 32, 'Mon,Tue,Wed,Fri', '09:00-15:00'),
('Dr. Henry Edwards', 'Psychiatrist', '555-0128', 'henry.edwards@hospital.com', 33, 'Tue,Thu', '12:00-20:00'),
('Dr. Abigail Collins', 'ENT Specialist', '555-0129', 'abigail.collins@hospital.com', 34, 'Mon,Wed,Thu', '08:00-14:00'),
('Dr. Sebastian Stewart', 'Ophthalmologist', '555-0130', 'sebastian.stewart@hospital.com', 35, 'Tue,Fri', '10:00-16:00'),
('Dr. Ella Sanchez', 'Radiologist', '555-0131', 'ella.sanchez@hospital.com', 36, 'Mon,Tue,Wed', '07:00-15:00'),
('Dr. Jackson Morris', 'Urologist', '555-0132', 'jackson.morris@hospital.com', 37, 'Thu,Fri', '09:00-17:00'),
('Dr. Scarlett Rogers', 'Oncologist', '555-0133', 'scarlett.rogers@hospital.com', 38, 'Mon,Wed,Fri', '08:00-14:00'),
('Dr. Levi Reed', 'Pulmonologist', '555-0134', 'levi.reed@hospital.com', 39, 'Tue,Thu', '11:00-18:00'),
('Dr. Grace Cook', 'Endocrinologist', '555-0135', 'grace.cook@hospital.com', 40, 'Mon,Tue,Fri', '09:00-15:00');



-- Insert Admin User
INSERT INTO users (username, password, full_name, email, phone, role) VALUES
('admin', 'admin123', 'System Administrator', 'admin@hospital.com', '555-0000', 'admin');
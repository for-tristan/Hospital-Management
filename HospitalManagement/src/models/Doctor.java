package models;

public class Doctor {
    private int doctorId;
    private int userId;
    private String fullName;
    private String specialization;
    private String phone;
    private String email;
    private int experienceYears;
    private String availableDays;
    private String availableTime;
    private String status;
    
    public Doctor() {}
    
    public Doctor(String fullName, String specialization, String phone, String email, 
                  int experienceYears, String availableDays, String availableTime) {
        this.fullName = fullName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.experienceYears = experienceYears;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
        this.status = "available";
    }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }
    
    public String getAvailableTime() { return availableTime; }
    public void setAvailableTime(String availableTime) { this.availableTime = availableTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
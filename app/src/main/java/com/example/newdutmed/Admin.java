package com.example.newdutmed;

public class Admin {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private String role;  // Optional, if you're tracking roles within the Admin class

    // Default constructor required for Firestore data mapping
    public Admin() {
    }

    // Constructor with parameters
    public Admin(String firstName, String lastName, String email, String phoneNumber, String department, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.role = role;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }
}



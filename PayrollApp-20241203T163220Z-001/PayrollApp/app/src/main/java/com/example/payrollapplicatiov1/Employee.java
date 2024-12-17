package com.example.payrollapplicatiov1;
import java.io.Serializable;

public class Employee implements Serializable {
    private String fullName;
    private String username, photo;
    private int workingHours;
    private double salary;

    // No-argument constructor
    public Employee() {
    }

    // Parameterized constructor
    public Employee(String fullName, String username, String photo, int workingHours, double salary) {
        this.fullName = fullName;
        this.username = username;
        this.photo = photo;
        this.workingHours = workingHours;
        this.salary = salary;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

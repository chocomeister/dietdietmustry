package com.example.dietandnutritionapplication;

import android.graphics.Bitmap;

public class Nutritionist extends Profile {
    private String education;
    private String contactInfo;
    private String expertise;
    private String bio;
    private String profilePicture;
    private String name;

    // Default constructor
    public Nutritionist() {
        super();
        this.setRole("nutritionist");
        this.setStatus("active");
    }

    // Parameterized constructor
    public Nutritionist(String firstName,String username,String name,String phoneNumber, String password, String email,String gender, String role, String dateJoined,String education, String contactInfo, String expertise, String bio, String profilePicture) {
        super();
        this.setFirstName(firstName);
        this.setUsername(username);
        this.setLastName(name);
        this.setPhoneNumber(phoneNumber);
        this.setPassword(password);
        this.setEmail(email);
        this.education = education;
        this.setGender(gender);
        this.contactInfo = contactInfo;
        this.expertise = expertise;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.setRole("nutritionist");
        this.setStatus("active");
    }

    // Getter and Setter methods
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setName(String name) {
        this.setFirstName(name);
    }

    public String getName() {
        return name;
    }
}

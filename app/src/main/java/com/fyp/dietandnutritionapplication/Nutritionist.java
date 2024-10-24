package com.fyp.dietandnutritionapplication;

public class Nutritionist extends Profile {
    private String firstName;
    private String education;
    private String contactInfo;
    private String expertise;
    private String bio;
    private String profilePicture;
    private String specialization;
    private String experience;

    // Default constructor
    public Nutritionist() {
        super();
        this.setRole("nutritionist");
        this.setStatus("pending");
    }

    // Parameterized constructor
    public Nutritionist(String firstName,String username,String name,String phoneNumber, String email,String gender, String role, String specialization, String experience, String dateJoined,String education, String contactInfo, String expertise, String bio, String profilePicture) {
        super();
        this.setFirstName(firstName);
        this.setUsername(username);
        this.setLastName(name);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
        this.education = education;
        this.setGender(gender);
        this.contactInfo = contactInfo;
        this.expertise = expertise;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.setSpecialization(specialization);
        this.setExperience(experience);
        this.setRole("nutritionist");
        this.setStatus("pending");
    }

    //for consulationUFragement class
    public Nutritionist(String firstName, String email, String profilePicture) {
        this.firstName = firstName;
        this.setEmail(email);
        this.profilePicture = profilePicture;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public void setName(String firstName) {
        this.setFirstName(firstName);
    }

    public String getName() {
        return firstName;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName(); // Combine first and last name
    }
}

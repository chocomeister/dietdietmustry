package com.fyp.dietandnutritionapplication;

import java.io.Serializable;

public class Consultation implements Serializable {

    private String consultationId;
    private String nutritionistName;
    private String clientName;
    private String date;
    private String time;
    private String status;
    private String zoomLink;
    private int price;

    // Default constructor (required for Firebase)
    public Consultation() {
    }

    // Parameterized constructor
    public Consultation(String consultationId, String nutritionistName, String clientName, String date, String time, String status, int price, String zoomLink) {
        this.consultationId = consultationId;
        this.nutritionistName = nutritionistName;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.price = price;
        this.zoomLink = zoomLink;
    }

    // Getters and setters for each field
    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getNutritionistName() {
        return nutritionistName;
    }

    public void setNutritionistName(String nutritionistName) {
        this.nutritionistName = nutritionistName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDate() {
        return date;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setZoomLink(String zoomLink) {
        this.zoomLink = zoomLink;
    }

    public String getZoomLink() {
        return zoomLink;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "consultationId='" + consultationId + '\'' +
                ", nutritionistName='" + nutritionistName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public char[] getId() {
        if (consultationId != null) {
            return consultationId.toCharArray();  // Convert the String to char[]
        }
        return null;  // Return null if the ID is not set
    }
}

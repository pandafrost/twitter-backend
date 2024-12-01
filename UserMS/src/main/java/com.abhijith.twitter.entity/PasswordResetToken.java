package com.abhijith.twitter.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "passwordResetTokens")
public class PasswordResetToken {

    @Id
    private String id;

    private String token;

    @DBRef
    private User user;

    private Date expiryDate;

    // Constructors, getters, and setters

    public PasswordResetToken() {}

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    // Method to check if the token is expired
    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }

    // Helper method to calculate the expiry date
    private Date calculateExpiryDate() {
        long now = System.currentTimeMillis();
        long expiryInMillis = 24 * 60 * 60 * 1000; // Token valid for 24 hours
        return new Date(now + expiryInMillis);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}


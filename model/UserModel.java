package com.docoding.clickcare.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String userUID;
    public String username;
    public String email;
    public String image;
    public String token;
    public String password;
    public String confirmPassword;

    public UserModel(String image, String token,String userUID, String username, String email, String password, String confirmPassword) {
        this.userUID = userUID;
        this.username = username;
        this.email = email;
        this.image = image;
        this.token = token;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserModel() {
    }

    public UserModel(String uid, String trim, String trim1, String trim2, String trim3) {
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


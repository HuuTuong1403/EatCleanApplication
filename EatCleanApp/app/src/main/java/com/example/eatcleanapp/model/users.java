package com.example.eatcleanapp.model;

import com.android.volley.RequestQueue;

public class users {
    private String IDUser;
    private String Email;
    private String Password;
    private String FullName;
    private String 	Image;
    private String LoginFB;
    private String IDRole;

    public String getLoginFB() {
        return LoginFB;
    }

    public void setLoginFB(String loginFB) {
        LoginFB = loginFB;
    }

    public users(String IDUser, String email, String password,
                 String fullName, String image, String loginFB, String IDRole) {
        this.IDUser = IDUser;
        Email = email;
        Password = password;
        FullName = fullName;
        Image = image;
        LoginFB = loginFB;
        this.IDRole = IDRole;
    }


    public String getIDUser() {
        return IDUser;
    }

    public void setIDUser(String IDUser) {
        this.IDUser = IDUser;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public String getIDRole() {
        return IDRole;
    }

    public void setIDRole(String IDRole) {
        this.IDRole = IDRole;
    }

}

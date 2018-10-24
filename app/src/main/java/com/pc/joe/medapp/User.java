package com.pc.joe.medapp;

import java.io.Serializable;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String emailAddress;
    private String userType;

    public User(String lastName, String userName, String password, String emailAddress, String firstName) {
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
    }

    public User() {
        this.lastName = "null";
        this.userName = "null";
        this.password = "null";
        this.emailAddress = "null";
        this.firstName = "null";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}

package com.example.inclass08;

import java.io.Serializable;

public class User implements Serializable {
    String email;
    String fullname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public User(String email, String fullname) {
        this.email = email;
        this.fullname = fullname;
    }
}

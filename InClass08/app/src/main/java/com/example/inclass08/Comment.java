package com.example.inclass08;

import java.io.Serializable;

public class Comment implements Serializable {
    String name,email,desc;
    String Date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Comment(String name, String email, String desc) {
        this.name = name;
        this.email = email;
        this.desc = desc;
        Date = Utils.HelperFunctions.getDate();
    }
}

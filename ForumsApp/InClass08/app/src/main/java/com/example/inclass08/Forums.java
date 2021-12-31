package com.example.inclass08;

import java.util.ArrayList;

public class Forums {



    String created_date;
    String creator;
    String creator_email;
    String desciption;
    String title;
    String id;
    Boolean isOwner;
    ArrayList<String> likes;
    ArrayList<Comment> comments;

    @Override
    public String toString() {
        return "Forums{" +
                "created_date='" + created_date + '\'' +
                ", creator='" + creator + '\'' +
                ", creator_email='" + creator_email + '\'' +
                ", desciption='" + desciption + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator_email() {
        return creator_email;
    }

    public void setCreator_email(String creator_email) {
        this.creator_email = creator_email;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Forums(String created_date, String creator, String creator_email, String desciption, String title, String id,Boolean isOwner) {
        this.created_date = created_date;
        this.creator = creator;
        this.creator_email = creator_email;
        this.desciption = desciption;
        this.title = title;
        this.id = id;
        this.isOwner = isOwner;
        this.likes = new ArrayList<String>();
    }

    public Forums(String created_date, String creator, String creator_email, String desciption, String title, String id,Boolean isOwner, ArrayList<String> likes,ArrayList<Comment>comments) {
        this.created_date = created_date;
        this.creator = creator;
        this.creator_email = creator_email;
        this.desciption = desciption;
        this.title = title;
        this.id = id;
        this.isOwner = isOwner;
        if(likes ==null)
        {
            this.likes = new ArrayList<String>();
        }
        else
        {
            this.likes = likes;
        }
        if(comments == null)
        {
            this.comments = new ArrayList<>();
        }
        else
        {
            this.comments = comments;
        }

    }
}

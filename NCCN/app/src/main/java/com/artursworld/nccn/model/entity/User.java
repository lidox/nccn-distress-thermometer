package com.artursworld.nccn.model.entity;


import java.util.Date;

public class User {

    private String name;
    private Date creationDate;

    public User(String name){
        this.name = name;
        this.creationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

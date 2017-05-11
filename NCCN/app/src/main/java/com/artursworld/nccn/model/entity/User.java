package com.artursworld.nccn.model.entity;


import com.artursworld.nccn.model.persistence.manager.EntityDbManager;

import java.util.Date;

public class User {

    private String name = null;
    private Date creationDate = null;
    private Date birthDate = null;
    private Gender gender = null;

    public User(String name) {
        setName(name);
        this.creationDate = new Date();
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("name: " + this.getName() + "\n");
        if (gender != null)
            ret.append("gender: " + this.getGender().name() + "\n");
        if (birthDate != null)
            ret.append("birthdate: " + EntityDbManager.dateFormat.format(birthDate) + "\n");
        if (creationDate != null)
            ret.append("creationdate: " + EntityDbManager.dateFormat.format(creationDate));
        return ret.toString();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

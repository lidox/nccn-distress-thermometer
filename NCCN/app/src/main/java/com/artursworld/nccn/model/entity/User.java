package com.artursworld.nccn.model.entity;


import com.artursworld.nccn.model.persistence.manager.EntityDbManager;

import java.util.Date;

public class User {

    private String name = null;
    private Date creationDate = null;

    public User(String name){
        setName(name);
        this.creationDate = new Date();
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append(this.getName());
        //ret.append(EntityDbManager.dateFormat.format(creationDate));
        return ret.toString();
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
        if(name != null)
            name = name.trim();
        this.name = name;
    }
}

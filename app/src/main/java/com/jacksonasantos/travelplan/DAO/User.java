package com.jacksonasantos.travelplan.DAO;

import java.sql.Date;

public class User {
    public int id;
    public String username;
    public String email;
    public Date createdDate;

    User() {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
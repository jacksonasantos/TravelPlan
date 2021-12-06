package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class Achievement {
    public Integer id;
    public String name;
    public String city;
    public String state;
    public String country;
    public String status;
    public String note;
    public String latlng_achievement;

    public Achievement() {
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.status = status;
        this.note = note;
        this.latlng_achievement = latlng_achievement;
    }

    @NonNull
    @Override
    public String toString() { return name; }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city; }

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getCountry() { return country;    }
    public void setCountry(String country) { this.country = country; }

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note; }

    public String getLatlng_achievement() {return latlng_achievement;}
    public void setLatlng_achievement(String latlng_achievement) {this.latlng_achievement = latlng_achievement;}
}

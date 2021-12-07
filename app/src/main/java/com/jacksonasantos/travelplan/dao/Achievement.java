package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class Achievement {
    public Integer id;
    public Integer travel_id;
    public String short_name;
    public String name;
    // TODO - Ajustar o tipo de armazenamento da imagem
    public String image;
    public String city;
    public String state;
    public String country;
    public String note;
    public String latlng_achievement;
    public int status_achievement;

    public Achievement() {
        this.travel_id = travel_id;
        this.short_name = short_name;
        this.name = name;
        this.image = image;
        this.city = city;
        this.state = state;
        this.country = country;
        this.note = note;
        this.latlng_achievement = latlng_achievement;
        this.status_achievement = status_achievement;
    }

    @NonNull
    @Override
    public String toString() { return name; }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public String getShort_name() {return short_name;}
    public void setShort_name(String short_name) {this.short_name = short_name;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city; }

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getCountry() { return country;    }
    public void setCountry(String country) { this.country = country; }

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note; }

    public String getLatlng_achievement() {return latlng_achievement;}
    public void setLatlng_achievement(String latlng_achievement) {this.latlng_achievement = latlng_achievement;}

    public int getStatus_achievement() {return status_achievement;}
    public void setStatus_achievement(int status_achievement) {this.status_achievement = status_achievement;}
}

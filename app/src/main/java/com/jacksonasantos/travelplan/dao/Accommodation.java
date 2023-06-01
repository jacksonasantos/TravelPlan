package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class Accommodation {
    
    public Integer id;
    public String name;
    public String address;
    public String city;
    public String state;
    public String country;
    public String contact_name;
    public String phone;
    public String email;
    public String site;
    public String latlng_accommodation;
    public int accommodation_type;

    public Accommodation() {
    }

    @NonNull
    @Override
    public String toString() { return name==null?"":name+"\n"+city+"/"+state; }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city; }

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getCountry() { return country;    }
    public void setCountry(String country) { this.country = country; }

    public String getContact_name() {return contact_name;}
    public void setContact_name(String contact_name) {this.contact_name = contact_name;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone; }

    public String getEmail() {return email; }
    public void setEmail(String email) { this.email = email;}

    public String getSite() {return site;}
    public void setSite(String site) {this.site = site;}

    public String getLatlng_accommodation() {return latlng_accommodation;}
    public void setLatlng_accommodation(String latlng_accommodation) {this.latlng_accommodation = latlng_accommodation;}

    public int getAccommodation_type() {return accommodation_type;}
    public void setAccommodation_type(int accommodation_type) {this.accommodation_type = accommodation_type;}
}

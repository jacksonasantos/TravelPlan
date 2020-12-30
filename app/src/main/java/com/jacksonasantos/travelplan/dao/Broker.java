package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class Broker {
    public Integer id;
    public String name;
    public String phone;
    public String email;
    public String contact_name;

    public Broker() {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.contact_name = contact_name;
    }

    @NonNull
    @Override
    public String toString() { return name; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact_name() { return contact_name; }
    public void setContact_name(String contact_name) { this.contact_name = contact_name; }
}

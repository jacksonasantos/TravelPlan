package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class Broker {
    public Long id;
    public String name;
    public String phone;
    public String email;

    public Broker() {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() { return name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

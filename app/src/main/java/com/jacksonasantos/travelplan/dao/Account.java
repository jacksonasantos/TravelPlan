package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class Account {
    public Integer id;
    public int account_type;
    public String description;

    public Account() {
    }

    @NonNull
    @Override
    public String toString() { return description; }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public int getAccount_type() {return account_type;}
    public void setAccount_type(int account_type) {this.account_type = account_type;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}

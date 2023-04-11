package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import java.util.Date;

public class Person {
    public Integer id;
    public String name;
    public String short_name;
    public int gender;
    public Date date_birth;
    public String driving_record;
    public Date license_expiration_date;
    public Date first_license_date;
    public Date license_issue_date;
    public String license_category;

    public Person() {
    }

    @NonNull
    public String toString() { return name; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShort_Name() { return short_name; }
    public void setShort_Name(String short_name) { this.short_name = short_name; }

    public int getGender() { return gender; }
    public void setGender(int gender) { this.gender = gender; }

    public Date getDate_birth() { return date_birth; }
    public void setDate_birth(Date date_birth) { this.date_birth = date_birth; }

    public String getDriving_record() { return driving_record;  }
    public void setDriving_record(String driving_record) { this.driving_record = driving_record; }

    public Date getLicense_expiration_date() { return license_expiration_date; }
    public void setLicense_expiration_date(Date license_expiration_date) { this.license_expiration_date = license_expiration_date; }

    public Date getFirst_license_date() { return first_license_date; }
    public void setFirst_license_date(Date first_license_date) { this.first_license_date = first_license_date; }

    public Date getLicense_issue_date() { return license_issue_date; }
    public void setLicense_issue_date(Date license_issue_date) { this.license_issue_date = license_issue_date; }

    public String getLicense_category() { return license_category; }
    public void setLicense_category(String license_category) { this.license_category = license_category; }
}

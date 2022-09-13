package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import java.util.Date;

public class Driver {
    public Integer id;
    public String name;
    public String short_name;
    public int gender;
    public String driving_record;
    public Date license_expiration_date;
    public Date first_license_date;
    public Date licence_issue_date;
    public String category;

    public Driver() {
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

    public String getDriving_record() { return driving_record;  }
    public void setDriving_record(String driving_record) { this.driving_record = driving_record; }

    public Date getLicense_expiration_date() { return license_expiration_date; }
    public void setLicense_expiration_date(Date license_expiration_date) { this.license_expiration_date = license_expiration_date; }

    public Date getFirst_license_date() { return first_license_date; }
    public void setFirst_license_date(Date first_license_date) { this.first_license_date = first_license_date; }

    public Date getLicence_issue_date() { return licence_issue_date; }
    public void setLicence_issue_date(Date licence_issue_date) { this.licence_issue_date = licence_issue_date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

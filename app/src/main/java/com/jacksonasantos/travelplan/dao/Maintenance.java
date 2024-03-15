package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class Maintenance {
    public Integer id;
    public Integer vehicle_id;
    public String detail;
    public Date date;
    public int odometer;
    public int currency_type;
    public Double value;
    public String location;
    public String note;

    public Maintenance() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getOdometer() { return odometer; }
    public void setOdometer(int odometer) { this.odometer = odometer; }

    public int getCurrency_type() { return currency_type; }
    public void setCurrency_type(int currency_type) { this.currency_type = currency_type; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

}

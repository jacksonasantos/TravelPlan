package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class VehicleMaintenanceItem {
    public Integer vehicle_id;
    public int service_type;
    public Date date;
    public int odometer;
    public String description;
    public String note;

    public VehicleMaintenanceItem() {
    }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) {this.vehicle_id = vehicle_id; }

    public int getService_type() { return service_type; }
    public void setService_type(int service_type) { this.service_type = service_type; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getOdometer() { return odometer; }
    public void setOdometer(int odometer) { this.odometer = odometer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}

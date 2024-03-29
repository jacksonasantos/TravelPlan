package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class PendingVehicle {
    public Integer id;
    public Integer vehicle_id;
    public int service_type;
    public String note;
    public int currency_type;
    public Double expected_value;
    public int status_pending;

    public PendingVehicle() {
    }

    @NonNull
    @Override
    public String toString() { return note; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public int getService_type() { return service_type; }
    public void setService_type(int service_type) { this.service_type = service_type; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getCurrency_type() {return currency_type;}
    public void setCurrency_type(int currency_type) {this.currency_type = currency_type;}

    public Double getExpected_value() { return expected_value; }
    public void setExpected_value(Double expected_value) { this.expected_value = expected_value; }

    public int getStatus_pending() { return status_pending; }
    public void setStatus_pending(int status_pending) { this.status_pending = status_pending; }
}

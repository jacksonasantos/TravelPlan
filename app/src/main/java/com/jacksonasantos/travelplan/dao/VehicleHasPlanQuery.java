package com.jacksonasantos.travelplan.dao;

public class VehicleHasPlanQuery extends VehicleHasPlan {

    public int expiration_default;
    public int recurring_service;
    public int measure;

    public VehicleHasPlanQuery() {
    }

    public int getExpiration_default() { return expiration_default; }
    public void setExpiration_default(int expiration_default) { this.expiration_default = expiration_default; }

    public int getRecurring_service() { return recurring_service; }
    public void setRecurring_service(int recurring_service) { this.recurring_service = recurring_service; }

    public int getMeasure() { return measure; }
    public void setMeasure(int measure) { this.measure = measure; }
}

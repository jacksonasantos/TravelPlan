package com.jacksonasantos.travelplan.dao;

public class VehicleHasPlanQuery {
    public Integer id;
    public Integer vehicle_id;
    public Integer maintenance_plan_id;
    public int expiration;
    public int expiration_default;
    public int recurring_service;
    public int measure;

    public VehicleHasPlanQuery() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public Integer getMaintenance_plan_id() { return maintenance_plan_id; }
    public void setMaintenance_plan_id(Integer maintenance_plan_id) { this.maintenance_plan_id = maintenance_plan_id; }

    public int getExpiration() { return expiration; }
    public void setExpiration(int expiration) { this.expiration = expiration; }

    public int getExpiration_default() { return expiration_default; }
    public void setExpiration_default(int expiration_default) { this.expiration_default = expiration_default; }

    public int getRecurring_service() { return recurring_service; }
    public void setRecurring_service(int recurring_service) { this.recurring_service = recurring_service; }

    public int getMeasure() { return measure; }
    public void setMeasure(int measure) { this.measure = measure; }
}

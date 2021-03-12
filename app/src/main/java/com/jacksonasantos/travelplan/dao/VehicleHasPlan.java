package com.jacksonasantos.travelplan.dao;

public class VehicleHasPlan {
    public Integer id;
    public Integer vehicle_id;
    public Integer maintenance_plan_id;
    public int expiration;

    public VehicleHasPlan() {
        this.id = id;
        this.vehicle_id = vehicle_id;
        this.maintenance_plan_id = maintenance_plan_id;
        this.expiration = expiration;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public Integer getMaintenance_plan_id() { return maintenance_plan_id; }
    public void setMaintenance_plan_id(Integer maintenance_plan_id) { this.maintenance_plan_id = maintenance_plan_id; }

    public int getExpiration() { return expiration; }
    public void setExpiration(int expiration) { this.expiration = expiration; }
}

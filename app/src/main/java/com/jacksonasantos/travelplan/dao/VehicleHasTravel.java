package com.jacksonasantos.travelplan.dao;

public class VehicleHasTravel {
    public Integer vehicle_id;
    public Integer travel_id;

    public VehicleHasTravel() {
        this.vehicle_id = vehicle_id;
        this.travel_id = travel_id;
    }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public int getTravel_id() { return travel_id; }
    public void setTravel_id(int travel_id) { this.travel_id = travel_id; }

}

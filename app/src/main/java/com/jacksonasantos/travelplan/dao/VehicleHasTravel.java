package com.jacksonasantos.travelplan.dao;

public class VehicleHasTravel {
    public Integer vehicle_id;
    public Integer travel_id;
    public Integer driver_id;
    public int start_odometer;
    public int final_odometer;

    public VehicleHasTravel() {
    }

    public Integer getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Integer vehicle_id) { this.vehicle_id = vehicle_id; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getDriver_id() { return driver_id; }
    public void setDriver_id(Integer driver_id) { this.driver_id = driver_id; }

    public int getStart_odometer() { return start_odometer; }
    public void setStart_odometer(int start_odometer) { this.start_odometer = start_odometer; }

    public int getFinal_odometer() { return final_odometer; }
    public void setFinal_odometer(int final_odometer) { this.final_odometer = final_odometer; }
}

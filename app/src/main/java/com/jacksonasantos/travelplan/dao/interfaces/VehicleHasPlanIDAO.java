package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleHasPlan;

import java.util.List;

public interface VehicleHasPlanIDAO {
    VehicleHasPlan fetchVehicleHasPlanById(Integer vehicle_id, Integer maintenance_id);
    List<VehicleHasPlan> fetchAllVehicleHasPlan();
    List<VehicleHasPlan> fetchAllVehicleHasPlanByVehicle(Integer vehicle_id);
    boolean addVehicleHasPlan(VehicleHasPlan vehicleHasPlan);
    void deleteVehicleHasPlan(Integer vehicle_id, Integer maintenance_id);
    boolean updateVehicleHasPlan(VehicleHasPlan vehicleHasPlan);
}
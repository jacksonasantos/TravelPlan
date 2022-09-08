package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleHasPlan;

import java.util.List;

public interface VehicleHasPlanIDAO {
    VehicleHasPlan fetchVehicleHasPlanById(Integer id);
    VehicleHasPlan fetchVehicleHasPlanByFK(Integer vehicle_id, Integer maintenance_plan_id);
    List<VehicleHasPlan> fetchAllVehicleHasPlanByVehicle(Integer vehicle_id);
    boolean addVehicleHasPlan(VehicleHasPlan vehicleHasPlan);
    void deleteVehicleHasPlan(Integer id);
    boolean updateVehicleHasPlan(VehicleHasPlan vehicleHasPlan);
}
package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Vehicle;

import java.util.List;

public interface VehicleIDAO {
    Vehicle fetchVehicleById(Long id);
    List<Vehicle> fetchAllVehicles();
    boolean addVehicle(Vehicle vehicle);
    void deleteVehicle(int vehicleId);
    boolean updateVehicle(Vehicle vehicle);
}
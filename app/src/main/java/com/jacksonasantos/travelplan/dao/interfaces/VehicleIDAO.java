package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Vehicle;

import java.util.List;

public interface VehicleIDAO {
    Vehicle fetchVehicleById(int userId);
    List<Vehicle> fetchAllVehicles();
    boolean addVehicle(Vehicle vehicle);
    boolean deleteVehicle(int id);
    boolean updateVehicle(Vehicle vehicle);
}
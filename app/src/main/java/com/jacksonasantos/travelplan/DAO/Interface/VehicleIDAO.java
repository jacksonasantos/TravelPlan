package com.jacksonasantos.travelplan.DAO.Interface;

import com.jacksonasantos.travelplan.DAO.Vehicle;

import java.util.List;

public interface VehicleIDAO {
    Vehicle fetchVehicleById(int userId);
    List<Vehicle> fetchAllVehicles();
    boolean addVehicle(Vehicle vehicle);
    boolean deleteVehicle(int id);
    boolean updateVehicle(Vehicle vehicle);
}
package com.jacksonasantos.travelplan.DAO.Interface;

import com.jacksonasantos.travelplan.DAO.Vehicle;

import java.util.List;

public interface VehicleIDAO {
    Vehicle fetchUserById(int userId);
    List<Vehicle> fetchAllUsers();
    // add Vehicle
    boolean addVehicle(Vehicle vehicle);
    // add Vehicle in bulk
    boolean addVehicles(List<Vehicle> vehicle);
    boolean deleteAllVehicles();
    boolean deleteVehicle(int id);
}
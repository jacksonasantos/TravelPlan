package com.jacksonasantos.travelplan.dao.interfaces;

import android.database.Cursor;

import com.jacksonasantos.travelplan.dao.Vehicle;

import java.util.ArrayList;
import java.util.List;

public interface VehicleIDAO {
    Vehicle fetchVehicleById(Integer id);
    List<Vehicle> fetchAllVehicles();
    ArrayList<Vehicle> fetchArrayVehicles();
    Cursor selectVehicles();
    boolean addVehicle(Vehicle vehicle);
    void deleteVehicle(Integer id);
    boolean updateVehicle(Vehicle vehicle);
}
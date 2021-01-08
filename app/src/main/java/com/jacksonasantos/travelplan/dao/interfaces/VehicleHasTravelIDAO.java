package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleHasTravel;

import java.util.List;

public interface VehicleHasTravelIDAO {
    VehicleHasTravel fetchVehicleHasTravelById(Integer vehicle_id, Integer travel_id);
    List<VehicleHasTravel> fetchAllVehicleHasTravel();
    List<VehicleHasTravel> fetchAllVehicleHasTravelByTravel(Integer travel_id);
    boolean addVehicleHasTravel(VehicleHasTravel vehicleHasTravel);
    void deleteVehicleHasTravel(Integer vehicle_id, Integer travel_id);
    boolean updateVehicleHasTravel(VehicleHasTravel vehicleHasTravel);
}
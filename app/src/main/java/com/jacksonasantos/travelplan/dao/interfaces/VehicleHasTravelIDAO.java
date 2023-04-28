package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleHasTravel;

import java.util.List;

public interface VehicleHasTravelIDAO {
    VehicleHasTravel fetchAllVehicleHasTravelById(Integer id);
    List<VehicleHasTravel> fetchAllVehicleHasTravelByTravel(Integer travel_id);
    boolean addVehicleHasTravel(VehicleHasTravel vehicleHasTravel);
    void deleteVehicleHasTravel(Integer Id);
    boolean updateVehicleHasTravel(VehicleHasTravel vehicleHasTravel);
}
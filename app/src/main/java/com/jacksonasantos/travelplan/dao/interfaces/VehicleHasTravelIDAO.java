package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleHasTravel;

import java.util.List;

public interface VehicleHasTravelIDAO {
    VehicleHasTravel findVehicleHasTravelByID(Integer vehicle_has_travel_id);
    List<VehicleHasTravel> fetchAllVehicleHasTravelByTravelForFuel(Integer travel_id);
    List<VehicleHasTravel> fetchAllVehicleHasTravelByTravel(Integer travel_id);
    VehicleHasTravel findVehicleHasTravelByTravelVehicle(Integer travel_id, Integer vehicle_id);
    VehicleHasTravel findVehicleHasTravelByTravelTransport(Integer travel_id, Integer transport_id);
    boolean addVehicleHasTravel(VehicleHasTravel vehicleHasTravel);
    void deleteVehicleHasTravel(Integer Id);
    boolean updateVehicleHasTravel(VehicleHasTravel vehicleHasTravel);
}
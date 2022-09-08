package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.FuelSupply;

import java.util.List;

public interface FuelSupplyIDAO {
    FuelSupply fetchFuelSupplyById(Integer id);
    List<FuelSupply> fetchAllFuelSupplyHasTravelByTravel(Integer travel_id);
    FuelSupply findLastFuelSupply(Integer vehicle_id);
    FuelSupply findAVGConsumptionTravel(Integer vehicle_id, Integer travel_id);
    List<FuelSupply> fetchAllFuelSupplies(boolean descOrder);
    boolean addFuelSupply(FuelSupply fuelSupply);
    void deleteFuelSupply(Integer id);
    boolean updateFuelSupply(FuelSupply fuelSupply);
}
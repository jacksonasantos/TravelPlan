package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.FuelSupply;

import java.util.List;

public interface FuelSupplyIDAO {
    FuelSupply fetchFuelSupplyById(Integer id);
    List<FuelSupply> fetchAllFuelSupplies();
    boolean addFuelSupply(FuelSupply fuelSupply);
    void deleteFuelSupply(Integer id);
    boolean updateFuelSupply(FuelSupply fuelSupply);
}
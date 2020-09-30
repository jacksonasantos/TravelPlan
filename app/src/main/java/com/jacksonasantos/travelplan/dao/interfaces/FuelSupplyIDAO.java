package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.FuelSupply;

import java.util.List;

public interface FuelSupplyIDAO {
    FuelSupply fetchFuelSupplyById(Long id);
    List<FuelSupply> fetchAllFuelSupplies();
    boolean addFuelSupply(FuelSupply fuelSupply);
    void deleteFuelSupply(int fuelSupplyId);
    boolean updateFuelSupply(FuelSupply fuelSupply);
}
package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.FuelSupply;

import java.text.ParseException;
import java.util.List;

public interface FuelSupplyIDAO {
    FuelSupply fetchFuelSupplyById(Long id) throws ParseException;
    List<FuelSupply> fetchAllFuelSupplies() throws ParseException;
    boolean addFuelSupply(FuelSupply fuelSupply);
    void deleteFuelSupply(Long id);
    boolean updateFuelSupply(FuelSupply fuelSupply);
}
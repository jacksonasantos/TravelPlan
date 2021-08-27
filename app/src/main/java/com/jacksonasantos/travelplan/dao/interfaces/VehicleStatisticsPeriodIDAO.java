package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleStatisticsPeriod;

import java.util.List;

public interface VehicleStatisticsPeriodIDAO {
    List<VehicleStatisticsPeriod> findVehicleStatisticsPeriod(Integer vehicle_id);
    List<VehicleStatisticsPeriod> findVehicleStatisticsPeriod();
}
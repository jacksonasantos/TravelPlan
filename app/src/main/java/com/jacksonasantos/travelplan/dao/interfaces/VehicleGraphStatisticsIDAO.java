package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleGraphStatistics;

import java.util.List;

public interface VehicleGraphStatisticsIDAO {
    List<VehicleGraphStatistics> findLastVehicleGraphStatistics(Integer vehicle_id, Integer type);
}
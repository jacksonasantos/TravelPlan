package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleStatistics;

import java.util.List;

public interface VehicleStatisticsIDAO {
    List<VehicleStatistics> findTotalVehicleStatistics(Integer vehicle_id);
    List<VehicleStatistics> findLastVehicleStatistics(Integer vehicle_id);
    VehicleStatistics findLastVehicleStatistics(Integer vehicle_id, int reason_type);
    List<VehicleStatistics> findLastVehicleGraphStatistics(Integer vehicle_id, Integer type);
}
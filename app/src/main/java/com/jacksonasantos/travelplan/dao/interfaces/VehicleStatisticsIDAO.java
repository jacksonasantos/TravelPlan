package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleStatistics;

import java.util.List;

public interface VehicleStatisticsIDAO {
    VehicleStatistics fetchVehicleStatisticsById(Integer id);
    List<VehicleStatistics> fetchAllVehicleStatistics();
    List<VehicleStatistics> findVehicleStatisticsbyId(Integer id);
    List<VehicleStatistics> findLastVehicleStatistics(Integer vehicle_id);
    boolean addVehicleStatistics(VehicleStatistics vehicleStatistics);
    void deleteVehicleStatistics(Integer id);
    boolean updateVehicleStatistics(VehicleStatistics vehicleStatistics);
    boolean changeVehicleStatistics(VehicleStatistics vehicleStatistics);
}
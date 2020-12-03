package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleStatistics;

import java.util.List;

public interface VehicleStatisticsIDAO {
    VehicleStatistics fetchVehicleStatisticsById(Long id);
    List<VehicleStatistics> fetchAllVehicleStatistics();
    List<VehicleStatistics> findVehicleStatisticsbyId(Long id);
    boolean addVehicleStatistics(VehicleStatistics vehicleStatistics);
    void deleteVehicleStatistics(Long id);
    boolean updateVehicleStatistics(VehicleStatistics vehicleStatistics);
    boolean changeVehicleStatistics(VehicleStatistics vehicleStatistics);
}
package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleStatistics;
import com.jacksonasantos.travelplan.dao.VehicleStatisticsYearResponse;

import java.util.List;

public interface VehicleStatisticsIDAO {
    List<VehicleStatistics> findTotalFuelingVehicleStatistics(Integer vehicle_id);
    List<VehicleStatistics> findTotalFuelingTransportStatistics(Integer transport_id);
    List<VehicleStatisticsYearResponse> findVehicleMileageStatisticsYear(Integer vehicle_id);
    List<VehicleStatistics> findVehicleFuelingStatistics(Integer vehicle_id);
    VehicleStatistics findVehicleFuelingStatistics(Integer vehicle_id, int reason_type);
    List<VehicleStatistics> findVehicleFuelingGraphStatistics(Integer vehicle_id, Integer type);
}
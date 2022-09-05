package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.VehicleMaintenanceItem;

import java.util.List;

public interface VehicleMaintenanceItemlDAO {
    List<VehicleMaintenanceItem> findVehicleMaintenanceItems(Integer vehicle_id, Integer service_type);
}

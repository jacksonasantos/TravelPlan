package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.MaintenanceItem;

import java.util.List;

public interface MaintenanceItemIDAO {
    List<MaintenanceItem> fetchMaintenanceItemByMaintenance(Integer maintenance_id);
    boolean addMaintenanceItem(MaintenanceItem maintenanceItem);
    void deleteMaintenanceItem(Integer maintenance_id, Integer id);
}
package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Maintenance;
import com.jacksonasantos.travelplan.dao.MaintenanceItem;

import java.util.List;

public interface MaintenanceItemIDAO {
    MaintenanceItem fetchMaintenanceItemById(Integer maintenance_id, Integer id);
    List<MaintenanceItem> fetchMaintenanceItemByMaintenance(Integer maintenance_id);
    List<MaintenanceItem> fetchAllMaintenanceItem();
    boolean addMaintenanceItem(MaintenanceItem maintenanceItem);
    void deleteMaintenanceItem(Integer maintenance_id, Integer id);
    boolean updateMaintenanceItem(MaintenanceItem maintenanceItem);
}
package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Maintenance;

import java.util.List;

public interface MaintenanceIDAO {
    Maintenance fetchMaintenanceById(Integer id);
    List<Maintenance> fetchAllMaintenance();
    boolean addMaintenance(Maintenance maintenance);
    void deleteMaintenance(Integer id);
    boolean updateMaintenance(Maintenance maintenance);
}
package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Maintenance;

import java.util.List;

public interface MaintenanceIDAO {
    Maintenance fetchMaintenanceById(Integer id);
    List<Maintenance> fetchAllMaintenance();
    List<Maintenance> findReminderMaintenance( Integer id);
    boolean addMaintenance(Maintenance maintenance);
    void deleteMaintenance(Integer id);
    boolean updateMaintenance(Maintenance maintenance);
}
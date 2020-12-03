package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Maintenance;

import java.text.ParseException;
import java.util.List;

public interface MaintenanceIDAO {
    Maintenance fetchMaintenanceById(Long id);
    List<Maintenance> fetchAllMaintenance();
    List<Maintenance> findReminderMaintenance( Long id);
    boolean addMaintenance(Maintenance maintenance);
    void deleteMaintenance(Long id);
    boolean updateMaintenance(Maintenance maintenance);
}
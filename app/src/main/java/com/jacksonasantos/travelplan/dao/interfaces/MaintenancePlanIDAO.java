package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.MaintenancePlan;

import java.util.ArrayList;
import java.util.List;

public interface MaintenancePlanIDAO {
    ArrayList<MaintenancePlan> fetchArrayMaintenancePlan();
    MaintenancePlan fetchMaintenancePlanById(Integer id);
    MaintenancePlan fetchMaintenancePlanByService(int type, String description);
    List<MaintenancePlan> fetchAllMaintenancePlan();
    Integer addMaintenancePlan(MaintenancePlan maintenancePlan);
    void deleteMaintenancePlan(Integer id);
    boolean updateMaintenancePlan(MaintenancePlan maintenancePlan);
}
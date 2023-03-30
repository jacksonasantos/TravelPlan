package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.MaintenancePlanHasVehicleType;

import java.util.List;

public interface MaintenancePlanHasVehicleTypeIDAO {
    MaintenancePlanHasVehicleType fetchMaintenancePlanHasVehicleTypeById(Integer id);
    List<MaintenancePlanHasVehicleType> fetchMaintenancePlanHasVehicleTypeByPlan(Integer maintenance_plan_id);
    boolean addMaintenancePlanHasVehicleType(MaintenancePlanHasVehicleType maintenancePlanHasVehicleType);
    void deleteMaintenancePlanHasVehicleType(Integer id);
    boolean updateMaintenancePlanHasVehicleType(MaintenancePlanHasVehicleType maintenancePlanHasVehicleType);
}
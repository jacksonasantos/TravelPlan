package com.jacksonasantos.travelplan.dao;

public class MaintenancePlanHasVehicleType {
    public Integer id;
    public Integer maintenance_plan_id;
    public int service_type;
    public int recurring_service;

    public MaintenancePlanHasVehicleType() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMaintenance_plan_id() { return maintenance_plan_id; }
    public void setMaintenance_plan_id(Integer maintenance_plan_id) { this.maintenance_plan_id = maintenance_plan_id; }

    public int getService_type() { return service_type; }
    public void setService_type(int service_type) { this.service_type = service_type; }

    public int getRecurring_service() { return recurring_service; }
    public void setRecurring_service(int recurring_service) { this.recurring_service = recurring_service; }
}
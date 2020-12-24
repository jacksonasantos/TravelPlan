package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleHasPlanISchema {

    String VEHICLE_HAS_PLAN_TABLE = "vehicle_has_plan";

    String VEHICLE_HAS_PLAN_VEHICLE_ID = "vehicle_id";
    String VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID = "maintenance_plan_id";
    String VEHICLE_HAS_PLAN_EXPIRATION = "expiration";

    // Version 23
    String CREATE_TABLE_VEHICLE_HAS_PLAN_V23 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_HAS_PLAN_TABLE + " ("
            + VEHICLE_HAS_PLAN_VEHICLE_ID + " INTEGER REFERENCES " + VehicleISchema.VEHICLE_TABLE + " ("+VehicleISchema.VEHICLE_ID+"), "
            + VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " INTEGER REFERENCES " + MaintenancePlanISchema.MAINTENANCE_PLAN_TABLE + " ("+MaintenancePlanISchema.MAINTENANCE_PLAN_ID+"), "
            + VEHICLE_HAS_PLAN_EXPIRATION + " INT, "
            + " PRIMARY KEY ("+VEHICLE_HAS_PLAN_VEHICLE_ID+", "+ VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID+") "
            + ")";

    String[] VEHICLE_HAS_PLAN_COLUMNS = new String[] {
            VEHICLE_HAS_PLAN_VEHICLE_ID,
            VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID,
            VEHICLE_HAS_PLAN_EXPIRATION
    };
}
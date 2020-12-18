package com.jacksonasantos.travelplan.dao.interfaces;

public interface MaintenancePlanISchema {

    String MAINTENANCE_PLAN_TABLE = "maintenance_plan";

    String MAINTENANCE_PLAN_ID = "id";
    String MAINTENANCE_PLAN_SERVICE_TYPE = "service_type";
    String MAINTENANCE_PLAN_DESCRIPTION = "description";
    String MAINTENANCE_PLAN_MEASURE = "measure";
    String MAINTENANCE_PLAN_EXPIRATION = "expiration";
    String MAINTENANCE_PLAN_RECOMMENDATION = "recommendation";

    // Version 22
    String CREATE_TABLE_MAINTENANCE_PLAN_V22 = "CREATE TABLE IF NOT EXISTS "
            + MAINTENANCE_PLAN_TABLE + " ("
            + MAINTENANCE_PLAN_ID + " INTEGER PRIMARY KEY, "
            + MAINTENANCE_PLAN_SERVICE_TYPE + " INT, "
            + MAINTENANCE_PLAN_DESCRIPTION + " TEXT, "
            + MAINTENANCE_PLAN_MEASURE + " INT, "
            + MAINTENANCE_PLAN_EXPIRATION + " INT, "
            + MAINTENANCE_PLAN_RECOMMENDATION + " STRING "
            + ")";

    String[] MAINTENANCE_PLAN_COLUMNS = new String[] {
             MAINTENANCE_PLAN_ID,
             MAINTENANCE_PLAN_SERVICE_TYPE,
             MAINTENANCE_PLAN_DESCRIPTION,
             MAINTENANCE_PLAN_MEASURE,
             MAINTENANCE_PLAN_EXPIRATION,
             MAINTENANCE_PLAN_RECOMMENDATION
    };
}

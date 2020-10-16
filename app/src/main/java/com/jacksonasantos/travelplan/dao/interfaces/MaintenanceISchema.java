package com.jacksonasantos.travelplan.dao.interfaces;

public interface MaintenanceISchema {

    String MAINTENANCE_TABLE = "maintenance";

    String MAINTENANCE_ID = "rowid";
    String MAINTENANCE_VEHICLE_ID = "vehicle_id";
    String MAINTENANCE_TYPE = "type";
    String MAINTENANCE_DETAIL = "detail";
    String MAINTENANCE_DATE = "date";
    String MAINTENANCE_EXPIRATION_DATE = "expiration_date";
    String MAINTENANCE_EXPIRATION_KM = "expiration_km";
    String MAINTENANCE_ODOMETER = "odometer";
    String MAINTENANCE_VALUE = "value";
    String MAINTENANCE_LOCATION = "location";
    String MAINTENANCE_NOTE = "note";

    // Version 10
    String CREATE_TABLE_MAINTENANCE_V10 = "CREATE TABLE IF NOT EXISTS "
            + MAINTENANCE_TABLE + " ("
            + MAINTENANCE_VEHICLE_ID + " LONG, "
            + MAINTENANCE_TYPE + " INT, "
            + MAINTENANCE_DETAIL + " TEXT, "
            + MAINTENANCE_DATE + " DATE, "
            + MAINTENANCE_EXPIRATION_DATE + " DATE, "
            + MAINTENANCE_EXPIRATION_KM + " INT, "
            + MAINTENANCE_ODOMETER + " INT, "
            + MAINTENANCE_VALUE + " DOUBLE, "
            + MAINTENANCE_LOCATION + " STRING, "
            + MAINTENANCE_NOTE + " STRING "
            + ")";

    String[] MAINTENANCE_COLUMNS = new String[] {
            MAINTENANCE_ID,
            MAINTENANCE_VEHICLE_ID,
            MAINTENANCE_TYPE,
            MAINTENANCE_DETAIL,
            MAINTENANCE_DATE,
            MAINTENANCE_EXPIRATION_DATE,
            MAINTENANCE_EXPIRATION_KM,
            MAINTENANCE_ODOMETER,
            MAINTENANCE_VALUE,
            MAINTENANCE_LOCATION,
            MAINTENANCE_NOTE
    };
}

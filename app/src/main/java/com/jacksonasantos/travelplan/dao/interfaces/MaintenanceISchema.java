package com.jacksonasantos.travelplan.dao.interfaces;

public interface MaintenanceISchema {

    String MAINTENANCE_TABLE = "maintenance";

    String MAINTENANCE_ID = "id";
    String MAINTENANCE_VEHICLE_ID = "vehicle_id";
    String MAINTENANCE_DETAIL = "detail";
    String MAINTENANCE_DATE = "date";
    String MAINTENANCE_ODOMETER = "odometer";
    String MAINTENANCE_CURRENCY_TYPE = "currency_type";
    String MAINTENANCE_VALUE = "value";
    String MAINTENANCE_LOCATION = "location";
    String MAINTENANCE_NOTE = "note";

    // Version 10
    String CREATE_TABLE_MAINTENANCE_V10 = "CREATE TABLE IF NOT EXISTS "
            + MAINTENANCE_TABLE + " ("
            + MAINTENANCE_ID + " INTEGER PRIMARY KEY, "
            + MAINTENANCE_VEHICLE_ID + " INTEGER REFERENCES " + VehicleISchema.VEHICLE_TABLE + " ("+VehicleISchema.VEHICLE_ID+"), "
            + MAINTENANCE_DETAIL + " TEXT, "
            + MAINTENANCE_DATE + " DATE, "
            + MAINTENANCE_ODOMETER + " INT, "
            + MAINTENANCE_VALUE + " DOUBLE, "
            + MAINTENANCE_LOCATION + " STRING, "
            + MAINTENANCE_NOTE + " STRING "
            + ")";

    // Version 89
    String ALTER_TABLE_MAINTENANCE_V89 = "ALTER TABLE " + MAINTENANCE_TABLE
            + " ADD COLUMN " + MAINTENANCE_CURRENCY_TYPE + " STRING ";

    String[] MAINTENANCE_COLUMNS = new String[] {
            MAINTENANCE_ID,
            MAINTENANCE_VEHICLE_ID,
            MAINTENANCE_DETAIL,
            MAINTENANCE_DATE,
            MAINTENANCE_ODOMETER,
            MAINTENANCE_CURRENCY_TYPE,
            MAINTENANCE_VALUE,
            MAINTENANCE_LOCATION,
            MAINTENANCE_NOTE
    };
}

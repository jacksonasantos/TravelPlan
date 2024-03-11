package com.jacksonasantos.travelplan.dao.interfaces;

public interface PendingVehicleISchema {

    String PENDING_VEHICLE_TABLE = "pending_vehicle";

    String PENDING_VEHICLE_ID = "id";
    String PENDING_VEHICLE_VEHICLE_ID = "vehicle_id";
    String PENDING_VEHICLE_SERVICE_TYPE = "service_type";
    String PENDING_VEHICLE_NOTE = "note";
    String PENDING_VEHICLE_CURRENCY_TYPE = "currency_type";
    String PENDING_VEHICLE_EXPECTED_VALUE = "expected_value";
    String PENDING_VEHICLE_STATUS_PENDING = "status_pending";

    // Version 53
    String CREATE_TABLE_PENDING_VEHICLE_V53 = "CREATE TABLE IF NOT EXISTS "
            + PENDING_VEHICLE_TABLE + " ("
            + PENDING_VEHICLE_ID + " INTEGER PRIMARY KEY, "
            + PENDING_VEHICLE_VEHICLE_ID + " INTEGER REFERENCES " + VehicleISchema.VEHICLE_TABLE + " ("+VehicleISchema.VEHICLE_ID+"), "
            + PENDING_VEHICLE_SERVICE_TYPE + " INT, "
            + PENDING_VEHICLE_NOTE + " TEXT, "
            + PENDING_VEHICLE_EXPECTED_VALUE + " DOUBLE, "
            + PENDING_VEHICLE_STATUS_PENDING + " INT "
            + ")";

    // Version 85
    String ALTER_TABLE_PENDING_VEHICLE_V85 = "ALTER TABLE " + PENDING_VEHICLE_TABLE
            + " ADD COLUMN " + PENDING_VEHICLE_CURRENCY_TYPE + " INT ";

    String[] PENDING_VEHICLE_COLUMNS = new String[] {
            PENDING_VEHICLE_ID,
            PENDING_VEHICLE_VEHICLE_ID,
            PENDING_VEHICLE_SERVICE_TYPE,
            PENDING_VEHICLE_NOTE,
            PENDING_VEHICLE_CURRENCY_TYPE,
            PENDING_VEHICLE_EXPECTED_VALUE,
            PENDING_VEHICLE_STATUS_PENDING
    };
}

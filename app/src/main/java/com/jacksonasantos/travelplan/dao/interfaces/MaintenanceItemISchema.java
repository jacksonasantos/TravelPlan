package com.jacksonasantos.travelplan.dao.interfaces;

public interface MaintenanceItemISchema {

    String MAINTENANCE_ITEM_TABLE = "maintenance_item";

    String MAINTENANCE_ITEM_ID = "id";
    String MAINTENANCE_ITEM_MAINTENANCE_ID = "maintenance_id";
    String MAINTENANCE_ITEM_SERVICE_TYPE = "service_type";
    String MAINTENANCE_ITEM_MEASURE_TYPE = "measure_type";
    String MAINTENANCE_ITEM_EXPIRATION_VALUE = "expiration_value";
    String MAINTENANCE_ITEM_VALUE = "value";
    String MAINTENANCE_ITEM_NOTE = "note";

    // Version 29
    String CREATE_TABLE_MAINTENANCE_ITEM_V29 = "CREATE TABLE IF NOT EXISTS "
            + MAINTENANCE_ITEM_TABLE + " ("
            + MAINTENANCE_ITEM_ID + " INTEGER PRIMARY KEY, "
            + MAINTENANCE_ITEM_MAINTENANCE_ID + " INTEGER REFERENCES " + MaintenanceISchema.MAINTENANCE_TABLE + " ("+MaintenanceISchema.MAINTENANCE_ID+"), "
            + MAINTENANCE_ITEM_SERVICE_TYPE + " INT, "
            + MAINTENANCE_ITEM_MEASURE_TYPE + " INT, "
            + MAINTENANCE_ITEM_EXPIRATION_VALUE + " INT, "
            + MAINTENANCE_ITEM_VALUE + " DOUBLE, "
            + MAINTENANCE_ITEM_NOTE + " STRING "
            //+ "PRIMARY KEY (" +MAINTENANCE_ITEM_MAINTENANCE_ID + ", "+ MAINTENANCE_ITEM_ID + ") "
            + ")";

    String[] MAINTENANCE_ITEM_COLUMNS = new String[] {
            MAINTENANCE_ITEM_ID,
            MAINTENANCE_ITEM_MAINTENANCE_ID,
            MAINTENANCE_ITEM_SERVICE_TYPE,
            MAINTENANCE_ITEM_MEASURE_TYPE,
            MAINTENANCE_ITEM_EXPIRATION_VALUE,
            MAINTENANCE_ITEM_VALUE,
            MAINTENANCE_ITEM_NOTE
    };
}

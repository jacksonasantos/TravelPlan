package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleHasTravelISchema {

    String VEHICLE_HAS_TRAVEL_TABLE = "vehicle_has_travel";

    String VEHICLE_HAS_TRAVEL_VEHICLE_ID = "vehicle_id";
    String VEHICLE_HAS_TRAVEL_TRAVEL_ID = "travel_id";
    String VEHICLE_HAS_TRAVEL_DRIVER_ID = "driver_id";

    // Version 25
    String CREATE_TABLE_VEHICLE_HAS_TRAVEL_V25 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_HAS_TRAVEL_TABLE + " ("
            + VEHICLE_HAS_TRAVEL_VEHICLE_ID + " INTEGER REFERENCES " + VehicleISchema.VEHICLE_TABLE + " ("+VehicleISchema.VEHICLE_ID+"), "
            + VEHICLE_HAS_TRAVEL_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + " PRIMARY KEY ("+VEHICLE_HAS_TRAVEL_VEHICLE_ID+", "+ VEHICLE_HAS_TRAVEL_TRAVEL_ID+") "
            + ")";

    // Version 49
    String ALTER_TABLE_VEHICLE_HAS_TRAVEL_V49 = "ALTER TABLE " + VEHICLE_HAS_TRAVEL_TABLE + " ADD COLUMN " + VEHICLE_HAS_TRAVEL_DRIVER_ID + " INTEGER REFERENCES " + DriverISchema.DRIVER_TABLE + " ("+DriverISchema.DRIVER_ID+") ";

    String[] VEHICLE_HAS_TRAVEL_COLUMNS = new String[] {
            VEHICLE_HAS_TRAVEL_VEHICLE_ID,
            VEHICLE_HAS_TRAVEL_TRAVEL_ID,
            VEHICLE_HAS_TRAVEL_DRIVER_ID
    };
}
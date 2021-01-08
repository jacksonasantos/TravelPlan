package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleHasTravelISchema {

    String VEHICLE_HAS_TRAVEL_TABLE = "vehicle_has_travel";

    String VEHICLE_HAS_TRAVEL_VEHICLE_ID = "vehicle_id";
    String VEHICLE_HAS_TRAVEL_TRAVEL_ID = "travel_id";

    // Version 25
    String CREATE_TABLE_VEHICLE_HAS_TRAVEL_V25 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_HAS_TRAVEL_TABLE + " ("
            + VEHICLE_HAS_TRAVEL_VEHICLE_ID + " INTEGER REFERENCES " + VehicleISchema.VEHICLE_TABLE + " ("+VehicleISchema.VEHICLE_ID+"), "
            + VEHICLE_HAS_TRAVEL_TRAVEL_ID + " INTEGER REFERENCES " + TravelISchema.TRAVEL_TABLE + " ("+TravelISchema.TRAVEL_ID+"), "
            + " PRIMARY KEY ("+VEHICLE_HAS_TRAVEL_VEHICLE_ID+", "+ VEHICLE_HAS_TRAVEL_TRAVEL_ID+") "
            + ")";

    String[] VEHICLE_HAS_TRAVEL_COLUMNS = new String[] {
            VEHICLE_HAS_TRAVEL_VEHICLE_ID,
            VEHICLE_HAS_TRAVEL_TRAVEL_ID
    };
}
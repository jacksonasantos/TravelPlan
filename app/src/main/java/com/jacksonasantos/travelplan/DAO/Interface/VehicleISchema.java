package com.jacksonasantos.travelplan.DAO.Interface;

public interface VehicleISchema {

    String VEHICLE_TABLE = "vehicle";

    String VEHICLE_ID = "rowid";
    String VEHICLE_NAME = "name";
    String VEHICLE_LICENCE_PLATE = "license_plate";
    String VEHICLE_BRAND = "brand";
    String VEHICLE_TYPE_FUEL = "type_fuel";
    String VEHICLE_FULL_CAPACITY = "full_capacity";
    String VEHICLE_AVG_CONSUMPTION = "avg_consumption";

    // Version 1
    String CREATE_TABLE_VEHICLE_V1 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_TABLE + " ("
            + VEHICLE_NAME + " TEXT NOT NULL, "
            + VEHICLE_LICENCE_PLATE + " TEXT, "
            + VEHICLE_FULL_CAPACITY + " INT, "
            + VEHICLE_AVG_CONSUMPTION + " DOUBLE "
            + ")";

    // Version 2
    String ALTER_TABLE_VEHICLE_V2 = "ALTER TABLE " + VEHICLE_TABLE
            + " ADD COLUMN " + VEHICLE_BRAND + " TEXT";

    // Version 3
    String ALTER_TABLE_VEHICLE_V3 = "ALTER TABLE " + VEHICLE_TABLE
            + " ADD COLUMN " + VEHICLE_TYPE_FUEL + " TEXT";

    String[] VEHICLE_COLUMNS = new String[] {VEHICLE_ID, VEHICLE_NAME, VEHICLE_LICENCE_PLATE, VEHICLE_FULL_CAPACITY, VEHICLE_AVG_CONSUMPTION, VEHICLE_BRAND, VEHICLE_TYPE_FUEL };
}



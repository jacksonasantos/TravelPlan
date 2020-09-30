package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleISchema {

    String VEHICLE_TABLE = "vehicle";

    String VEHICLE_ID = "rowid";
    String VEHICLE_TYPE = "type";
    String VEHICLE_NAME = "name";
    String VEHICLE_LICENCE_PLATE = "license_plate";
    String VEHICLE_BRAND = "brand";
    String VEHICLE_TYPE_FUEL = "type_fuel";
    String VEHICLE_FULL_CAPACITY = "full_capacity";
    String VEHICLE_AVG_CONSUMPTION = "avg_consumption";
    String VEHICLE_SHORT_NAME = "short_name";
    String VEHICLE_DT_ACQUISITION = "dt_acquisition";
    String VEHICLE_DT_SALE = "dt_sale";
    String VEHICLE_DT_ODOMETER = "dt_odometer";
    String VEHICLE_ODOMETER = "odometer";

    // Version 1
    String CREATE_TABLE_VEHICLE_V1 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_TABLE + " ("
            + VEHICLE_NAME + " TEXT NOT NULL, "
            + VEHICLE_LICENCE_PLATE + " TEXT, "
            + VEHICLE_FULL_CAPACITY + " INT, "
            + VEHICLE_AVG_CONSUMPTION + " FLOAT "
            + ")";

    // Version 2
    String ALTER_TABLE_VEHICLE_V2 = "ALTER TABLE " + VEHICLE_TABLE
            + " ADD COLUMN " + VEHICLE_BRAND + " TEXT";

    // Version 3
    String ALTER_TABLE_VEHICLE_V3 = "ALTER TABLE " + VEHICLE_TABLE
            + " ADD COLUMN " + VEHICLE_TYPE_FUEL + " TEXT";

    // Version 5
    String ALTER_TABLE_VEHICLE_V5 = "ALTER TABLE " + VEHICLE_TABLE
            + " ADD COLUMN " + VEHICLE_TYPE + " INT";

    // Version 6
    String ALTER_TABLE_VEHICLE_V6 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_SHORT_NAME + " TEXT";

    // Version 6
    String ALTER_TABLE_VEHICLE_V7_1 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_ACQUISITION + " TEXT";
    String ALTER_TABLE_VEHICLE_V7_2 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_SALE + " TEXT";
    String ALTER_TABLE_VEHICLE_V7_3 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_ODOMETER + " TEXT";
    String ALTER_TABLE_VEHICLE_V7_4 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_ODOMETER + " INT";

    String[] VEHICLE_COLUMNS = new String[] {
            VEHICLE_ID,
            VEHICLE_TYPE,
            VEHICLE_NAME,
            VEHICLE_LICENCE_PLATE,
            VEHICLE_FULL_CAPACITY,
            VEHICLE_AVG_CONSUMPTION,
            VEHICLE_BRAND,
            VEHICLE_TYPE_FUEL,
            VEHICLE_SHORT_NAME,
            VEHICLE_DT_ACQUISITION,
            VEHICLE_DT_SALE,
            VEHICLE_DT_ODOMETER,
            VEHICLE_ODOMETER
    };
}



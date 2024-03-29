package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleISchema {

    String VEHICLE_TABLE = "vehicle";

    String VEHICLE_ID = "id";
    String VEHICLE_IMAGE = "image";
    String VEHICLE_VEHICLE_TYPE = "vehicle_type";
    String VEHICLE_NAME = "name";
    String VEHICLE_LICENCE_PLATE = "license_plate";
    String VEHICLE_OWNER_DRIVER_ID = "owner_driver_id";
    String VEHICLE_VEHICLE_OWNER_ID = "vehicle_owner_id";
    String VEHICLE_BRAND = "brand";
    String VEHICLE_FUEL_TYPE = "fuel_type";
    String VEHICLE_FULL_CAPACITY = "full_capacity";
    String VEHICLE_AVG_CONSUMPTION = "avg_consumption";
    String VEHICLE_AVG_COST_LITRE = "avg_cost_litre";
    String VEHICLE_SHORT_NAME = "short_name";
    String VEHICLE_DT_ACQUISITION = "dt_acquisition";
    String VEHICLE_ODOMETER_ACQUISITION = "odometer_acquisition";
    String VEHICLE_DT_SALE = "dt_sale";
    String VEHICLE_DT_ODOMETER = "dt_odometer";
    String VEHICLE_ODOMETER = "odometer";
    String VEHICLE_MODEL = "model";
    String VEHICLE_COLOR = "color";
    String VEHICLE_COLOR_CODE = "color_code";
    String VEHICLE_YEAR_MODEL = "year_model";
    String VEHICLE_YEAR_MANUFACTURE = "year_manufacture";
    String VEHICLE_VIN = "vin";
    String VEHICLE_LICENCE_NUMBER = "licence_number";
    String VEHICLE_STATE = "state";
    String VEHICLE_CITY = "city";
    String VEHICLE_DOORS = "doors";
    String VEHICLE_CAPACITY = "capacity";
    String VEHICLE_POWER = "power";
    String VEHICLE_ESTIMATED_VALUE = "estimated_value";
    String VEHICLE_DT_LAST_FUELING = "dt_last_fueling";
    String VEHICLE_LAST_SUPPLY_REASON_TYPE = "last_supply_reason_type";
    String VEHICLE_ACCUMULATED_NUMBER_LITERS = "accumulated_number_liters";
    String VEHICLE_ACCUMULATED_SUPPLY_VALUE = "accumulated_supply_value";

    // Version 1
    String CREATE_TABLE_VEHICLE_V1 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_TABLE + " ("
            + VEHICLE_ID + " INTEGER PRIMARY KEY, "
            + VEHICLE_NAME + " TEXT NOT NULL, "
            + VEHICLE_LICENCE_PLATE + " TEXT, "
            + VEHICLE_FULL_CAPACITY + " INT, "
            + VEHICLE_AVG_CONSUMPTION + " FLOAT, "
            + VEHICLE_AVG_COST_LITRE + " FLOAT "
            + ")";

    // Version 2
    String ALTER_TABLE_VEHICLE_V2 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_BRAND + " TEXT";

    // Version 3
    String ALTER_TABLE_VEHICLE_V3 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_FUEL_TYPE + " INT";

    // Version 5
    String ALTER_TABLE_VEHICLE_V5 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_VEHICLE_TYPE + " INT";

    // Version 6
    String ALTER_TABLE_VEHICLE_V6 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_SHORT_NAME + " TEXT";

    // Version 6
    String ALTER_TABLE_VEHICLE_V7_1 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_ACQUISITION + " DATE";
    String ALTER_TABLE_VEHICLE_V7_2 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_SALE + " DATE";
    String ALTER_TABLE_VEHICLE_V7_3 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_ODOMETER + " DATE";
    String ALTER_TABLE_VEHICLE_V7_4 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_ODOMETER + " INT";

    // Version 11
    String ALTER_TABLE_VEHICLE_V11_1 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_MODEL + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_2 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_COLOR + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_3 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_YEAR_MODEL + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_4 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_YEAR_MANUFACTURE + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_5 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_VIN + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_6 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_LICENCE_NUMBER + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_7 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_STATE + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_8 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_CITY + " TEXT";
    String ALTER_TABLE_VEHICLE_V11_9 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DOORS + " INT";
    String ALTER_TABLE_VEHICLE_V11_10 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_CAPACITY + " INT";
    String ALTER_TABLE_VEHICLE_V11_11 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_POWER + " INT";
    String ALTER_TABLE_VEHICLE_V11_12 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_ESTIMATED_VALUE + " DOUBLE";

    // Version 28
    String ALTER_TABLE_VEHICLE_V28_1 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_DT_LAST_FUELING + " DATE";
    String ALTER_TABLE_VEHICLE_V28_2 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_LAST_SUPPLY_REASON_TYPE + " INT";
    String ALTER_TABLE_VEHICLE_V28_3 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_ACCUMULATED_NUMBER_LITERS + " DOUBLE";
    String ALTER_TABLE_VEHICLE_V28_4 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_ACCUMULATED_SUPPLY_VALUE + " DOUBLE";

    // Version 35
    String ALTER_TABLE_VEHICLE_V35 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_COLOR_CODE + " INT";

    // Version 40
    String ALTER_TABLE_VEHICLE_V40 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_IMAGE + " BLOB";

    // Version 48
    String ALTER_TABLE_VEHICLE_V48 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_OWNER_DRIVER_ID + " INTEGER REFERENCES " + PersonISchema.PERSON_TABLE + " ("+PersonISchema.PERSON_ID+") ";

    // Version 58
    String ALTER_TABLE_VEHICLE_V58 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_ODOMETER_ACQUISITION + " INT";

    // Version 60

    String ALTER_TABLE_VEHICLE_V60_2 = "ALTER TABLE " + VEHICLE_TABLE + " ADD COLUMN " + VEHICLE_VEHICLE_OWNER_ID + " INT";
    String ALTER_TABLE_VEHICLE_V60_3 = "ALTER TABLE " + VEHICLE_TABLE + " DROP COLUMN " + VEHICLE_OWNER_DRIVER_ID;
    String ALTER_TABLE_VEHICLE_V60_1 = "ALTER TABLE " + VEHICLE_TABLE + " RENAME COLUMN " + VEHICLE_OWNER_DRIVER_ID + " TO " + VEHICLE_VEHICLE_OWNER_ID + "  ";

    String[] VEHICLE_COLUMNS = new String[] {
            VEHICLE_ID,
            VEHICLE_IMAGE,
            VEHICLE_VEHICLE_TYPE,
            VEHICLE_NAME,
            VEHICLE_LICENCE_PLATE,
            VEHICLE_VEHICLE_OWNER_ID,
            VEHICLE_FULL_CAPACITY,
            VEHICLE_AVG_CONSUMPTION,
            VEHICLE_AVG_COST_LITRE,
            VEHICLE_BRAND,
            VEHICLE_FUEL_TYPE,
            VEHICLE_SHORT_NAME,
            VEHICLE_DT_ACQUISITION,
            VEHICLE_ODOMETER_ACQUISITION,
            VEHICLE_DT_SALE,
            VEHICLE_DT_ODOMETER,
            VEHICLE_ODOMETER,
            VEHICLE_MODEL,
            VEHICLE_COLOR,
            VEHICLE_COLOR_CODE,
            VEHICLE_YEAR_MODEL,
            VEHICLE_YEAR_MANUFACTURE,
            VEHICLE_VIN,
            VEHICLE_LICENCE_NUMBER,
            VEHICLE_STATE,
            VEHICLE_CITY,
            VEHICLE_DOORS,
            VEHICLE_CAPACITY,
            VEHICLE_POWER,
            VEHICLE_ESTIMATED_VALUE,
            VEHICLE_DT_LAST_FUELING,
            VEHICLE_LAST_SUPPLY_REASON_TYPE,
            VEHICLE_ACCUMULATED_NUMBER_LITERS,
            VEHICLE_ACCUMULATED_SUPPLY_VALUE
    };
}



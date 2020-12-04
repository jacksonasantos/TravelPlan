package com.jacksonasantos.travelplan.dao.interfaces;

public interface FuelSupplyISchema {

    String FUEL_SUPPLY_TABLE = "fuel_supply";

    String FUEL_SUPPLY_ID = "id";
    String FUEL_SUPPLY_VEHICLE_ID = "vehicle_id";
    String FUEL_SUPPLY_GAS_STATION = "gas_station";
    String FUEL_SUPPLY_GAS_STATION_LOCATION = "gas_station_location";
    String FUEL_SUPPLY_SUPPLY_DATE = "supply_date";
    String FUEL_SUPPLY_NUMBER_LITERS = "number_liters";
    String FUEL_SUPPLY_COMBUSTIBLE = "combustible";
    String FUEL_SUPPLY_FULL_TANK = "full_tank";
    String FUEL_SUPPLY_CURRENCY_TYPE = "currency_type";
    String FUEL_SUPPLY_SUPPLY_VALUE = "supply_value";
    String FUEL_SUPPLY_FUEL_VALUE = "fuel_value";
    String FUEL_SUPPLY_VEHICLE_ODOMETER = "vehicle_odometer";
    String FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE = "vehicle_travelled_distance";
    String FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION = "stat_avg_fuel_consumption";
    String FUEL_SUPPLY_STAT_COST_PER_LITRE = "stat_cost_per_litre";
    String FUEL_SUPPLY_SUPPLY_REASON_TYPE = "supply_reason_type";
    String FUEL_SUPPLY_SUPPLY_REASON = "supply_reason";
    String FUEL_SUPPLY_ASSOCIATED_TRIP = "associated_trip";

    // Version 8
    String CREATE_TABLE_FUEL_SUPPLY_V8 = "CREATE TABLE IF NOT EXISTS "
            + FUEL_SUPPLY_TABLE + " ("
            + FUEL_SUPPLY_ID + " INTEGER PRIMARY KEY, "
            + FUEL_SUPPLY_VEHICLE_ID + " LONG REFERENCES " + VehicleISchema.VEHICLE_TABLE + ", "
            + FUEL_SUPPLY_GAS_STATION + " TEXT, "
            + FUEL_SUPPLY_GAS_STATION_LOCATION + " TEXT, "
            + FUEL_SUPPLY_SUPPLY_DATE + " DATE, "
            + FUEL_SUPPLY_NUMBER_LITERS + " DOUBLE, "
            + FUEL_SUPPLY_COMBUSTIBLE + " INT, "
            + FUEL_SUPPLY_FULL_TANK + " INT, "
            + FUEL_SUPPLY_CURRENCY_TYPE + " INT, "
            + FUEL_SUPPLY_SUPPLY_VALUE + " DOUBLE, "
            + FUEL_SUPPLY_FUEL_VALUE + " DOUBLE, "
            + FUEL_SUPPLY_VEHICLE_ODOMETER + " INT, "
            + FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " INT, "
            + FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION + " FLOAT, "
            + FUEL_SUPPLY_STAT_COST_PER_LITRE + " FLOAT, "
            + FUEL_SUPPLY_SUPPLY_REASON + " TEXT, "
            + FUEL_SUPPLY_ASSOCIATED_TRIP + " LONG "
            + ")";

    // Version 9
    String ALTER_TABLE_FUEL_SUPPLY_V9 = "ALTER TABLE " + FUEL_SUPPLY_TABLE
            + " ADD COLUMN " + FUEL_SUPPLY_SUPPLY_REASON_TYPE + " INT";

    String[] FUEL_SUPPLY_COLUMNS = new String[] {
            FUEL_SUPPLY_ID,
            FUEL_SUPPLY_VEHICLE_ID,
            FUEL_SUPPLY_GAS_STATION,
            FUEL_SUPPLY_GAS_STATION_LOCATION,
            FUEL_SUPPLY_SUPPLY_DATE,
            FUEL_SUPPLY_NUMBER_LITERS,
            FUEL_SUPPLY_COMBUSTIBLE,
            FUEL_SUPPLY_FULL_TANK,
            FUEL_SUPPLY_CURRENCY_TYPE,
            FUEL_SUPPLY_SUPPLY_VALUE,
            FUEL_SUPPLY_FUEL_VALUE,
            FUEL_SUPPLY_VEHICLE_ODOMETER,
            FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE,
            FUEL_SUPPLY_STAT_AVG_FUEL_CONSUMPTION,
            FUEL_SUPPLY_STAT_COST_PER_LITRE,
            FUEL_SUPPLY_SUPPLY_REASON_TYPE,
            FUEL_SUPPLY_SUPPLY_REASON,
            FUEL_SUPPLY_ASSOCIATED_TRIP
    };
}

package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleStatisticsISchema {
    String VEHICLE_STATISTICS_TABLE = "vehicle_statistics";

    String VEHICLE_STATISTICS_ID = "id";
    String VEHICLE_STATISTICS_VEHICLE_ID = "vehicle_id";
    String VEHICLE_STATISTICS_STATISTIC_DATE = "statistic_date";
    String VEHICLE_STATISTICS_SUPPLY_REASON_TYPE = "supply_reason_type";
    String VEHICLE_STATISTICS_AVG_CONSUMPTION = "avg_consumption";
    String VEHICLE_STATISTICS_AVG_COST_LITRE = "avg_cost_litre";

    // Version 14
    String CREATE_TABLE_VEHICLE_STATISTICS_V14 = "CREATE TABLE IF NOT EXISTS "
            + VEHICLE_STATISTICS_TABLE + " ("
            + VEHICLE_STATISTICS_ID + " INTEGER PRIMARY KEY, "
            + VEHICLE_STATISTICS_VEHICLE_ID + " INTEGER REFERENCES " + VehicleISchema.VEHICLE_TABLE + " ("+VehicleISchema.VEHICLE_ID+"), "
            + VEHICLE_STATISTICS_STATISTIC_DATE + " DATE, "
            + VEHICLE_STATISTICS_SUPPLY_REASON_TYPE + " INT, "
            + VEHICLE_STATISTICS_AVG_CONSUMPTION + " FLOAT "
            + ")";

    // Version 21
    String ALTER_TABLE_VEHICLE_STATISTICS_V21 = "ALTER TABLE " + VEHICLE_STATISTICS_TABLE
            + " ADD COLUMN " + VEHICLE_STATISTICS_AVG_COST_LITRE + " FLOAT ";

    String[] VEHICLE_STATISTICS_COLUMNS = new String[] {
            VEHICLE_STATISTICS_ID,
            VEHICLE_STATISTICS_VEHICLE_ID,
            VEHICLE_STATISTICS_STATISTIC_DATE,
            VEHICLE_STATISTICS_SUPPLY_REASON_TYPE,
            VEHICLE_STATISTICS_AVG_CONSUMPTION,
            VEHICLE_STATISTICS_AVG_COST_LITRE
    };
}

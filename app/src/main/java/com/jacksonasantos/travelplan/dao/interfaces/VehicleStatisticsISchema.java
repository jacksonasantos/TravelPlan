package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleStatisticsISchema {

    String VEHICLE_STATISTICS_VEHICLE_ID = "vehicle_id";
    String VEHICLE_STATISTICS_SUPPLY_REASON_TYPE = "supply_reason_type";
    String VEHICLE_STATISTICS_STATISTIC_DATE = "statistic_date";
    String VEHICLE_STATISTICS_AVG_CONSUMPTION = "avg_consumption";
    String VEHICLE_STATISTICS_AVG_COST_LITRE = "avg_cost_litre";
    String VEHICLE_STATISTICS_AVG_MAX_CONSUMPTION = "avg_max_consumption";

    String[] VEHICLE_STATISTICS_COLUMNS = new String[] {
            VEHICLE_STATISTICS_VEHICLE_ID,
            VEHICLE_STATISTICS_SUPPLY_REASON_TYPE,
            VEHICLE_STATISTICS_STATISTIC_DATE,
            VEHICLE_STATISTICS_AVG_CONSUMPTION,
            VEHICLE_STATISTICS_AVG_COST_LITRE,
            VEHICLE_STATISTICS_AVG_MAX_CONSUMPTION
    };
}

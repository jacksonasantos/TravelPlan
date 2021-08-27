package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleStatisticsPeriodISchema {

    String VEHICLE_STATISTICS_PERIOD_VEHICLE_ID = "vehicle_id";
    String VEHICLE_STATISTICS_PERIOD_SHORT_NAME = "short_name";
    String VEHICLE_STATISTICS_PERIOD_STATISTIC_PERIOD = "statistic_period";
    String VEHICLE_STATISTICS_PERIOD_AVG_CONSUMPTION = "avg_consumption";

    String[] VEHICLE_STATISTICS_PERIOD_COLUMNS = new String[] {
            VEHICLE_STATISTICS_PERIOD_VEHICLE_ID,
            VEHICLE_STATISTICS_PERIOD_SHORT_NAME,
            VEHICLE_STATISTICS_PERIOD_STATISTIC_PERIOD,
            VEHICLE_STATISTICS_PERIOD_AVG_CONSUMPTION
    };
}

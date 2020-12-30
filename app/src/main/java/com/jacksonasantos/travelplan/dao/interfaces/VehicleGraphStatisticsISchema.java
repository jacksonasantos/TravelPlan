package com.jacksonasantos.travelplan.dao.interfaces;

public interface VehicleGraphStatisticsISchema {

    String VEHICLE_GRAPH_STATISTICS_VEHICLE_ID = "vehicle_id";
    String VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE = "supply_reason_type";
    String VEHICLE_GRAPH_STATISTICS_XAXIS_DATE = "xaxis_date";
    String VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION = "yaxis_avg_consumption";
    String VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE = "yaxis_avg_cost_litre";

    String[] VEHICLE_GRAPH_STATISTICS_COLUMNS = new String[] {
            VEHICLE_GRAPH_STATISTICS_VEHICLE_ID,
            VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE,
            VEHICLE_GRAPH_STATISTICS_XAXIS_DATE,
            VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION,
            VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE
    };
}

package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleGraphStatisticsIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleGraphStatisticsISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class VehicleGraphStatisticsDAO extends DbContentProvider implements VehicleGraphStatisticsISchema, VehicleGraphStatisticsIDAO {

    private Cursor cursor;

    public VehicleGraphStatisticsDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<VehicleGraphStatistics> findLastVehicleGraphStatistics(Integer vehicle_id, Integer type) {
        List<VehicleGraphStatistics> vehicleGraphStatisticsList = new ArrayList<>();
        cursor = super.rawQuery("SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + " " + VehicleGraphStatisticsISchema.VEHICLE_GRAPH_STATISTICS_VEHICLE_ID+", " +
                                                 FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + " " + VehicleGraphStatisticsISchema.VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE+ ", " +
                                                 FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_DATE + " " + VehicleGraphStatisticsISchema.VEHICLE_GRAPH_STATISTICS_XAXIS_DATE+ ", " +
                                                 "(SUM("+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS+") ) " + VehicleGraphStatisticsISchema.VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION+", " +
                                                 "(SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + ") ) "+ VehicleGraphStatisticsISchema.VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE+" " +
                                       "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                                      "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
                                        "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                                        "AND " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + "=? " +
                                   "GROUP BY " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID +
                                          ", " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE +
                                          ", " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_DATE + " " +
                                   "ORDER BY " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID +
                                          ", " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE +
                                          ", " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_DATE,
                new String[] { String.valueOf(vehicle_id), String.valueOf(type)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    VehicleGraphStatistics vehicleGraphStatistics = cursorToEntity(cursor);
                    vehicleGraphStatisticsList.add(vehicleGraphStatistics);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehicleGraphStatisticsList;
    }

    protected VehicleGraphStatistics cursorToEntity(Cursor cursor) {

        VehicleGraphStatistics vehicleGraphStatistics = new VehicleGraphStatistics();

        int vehicle_idIndex;
        int supply_reason_typeIndex;
        int xaxis_dateIndex;
        int yaxis_avg_consumptionIndex;
        int yaxis_avg_cost_litreIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_GRAPH_STATISTICS_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_VEHICLE_ID);
                vehicleGraphStatistics.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE) != -1) {
                supply_reason_typeIndex = cursor.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE);
                vehicleGraphStatistics.setSupply_reason_type(cursor.getInt(supply_reason_typeIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_GRAPH_STATISTICS_XAXIS_DATE) != -1) {
                xaxis_dateIndex = cursor.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_XAXIS_DATE);
                vehicleGraphStatistics.setXaxis_date(Utils.dateParse(cursor.getString(xaxis_dateIndex)));
            }
            if (cursor.getColumnIndex(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION) != -1) {
                yaxis_avg_consumptionIndex = cursor.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION);
                vehicleGraphStatistics.setYaxis_avg_consumption(cursor.getInt(yaxis_avg_consumptionIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE) != -1) {
                yaxis_avg_cost_litreIndex = cursor.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE);
                vehicleGraphStatistics.setYaxis_avg_cost_litre(cursor.getInt(yaxis_avg_cost_litreIndex));
            }
        }
        return vehicleGraphStatistics;
    }
}

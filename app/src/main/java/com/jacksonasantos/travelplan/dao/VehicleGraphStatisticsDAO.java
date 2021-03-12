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
                                                 "(SUM("+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS+"+"+FuelSupplyISchema.FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+") ) " + VehicleGraphStatisticsISchema.VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION+", " +
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

    protected VehicleGraphStatistics cursorToEntity(Cursor c) {
        VehicleGraphStatistics vehicleGraphStatistics = new VehicleGraphStatistics();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_GRAPH_STATISTICS_VEHICLE_ID) != -1)            {vehicleGraphStatistics.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE) != -1)    {vehicleGraphStatistics.setSupply_reason_type(c.getInt(c.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_SUPPLY_REASON_TYPE))); }
            if (c.getColumnIndex(VEHICLE_GRAPH_STATISTICS_XAXIS_DATE) != -1)            {vehicleGraphStatistics.setXaxis_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_XAXIS_DATE)))); }
            if (c.getColumnIndex(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION) != -1) {vehicleGraphStatistics.setYaxis_avg_consumption(c.getInt(c.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_CONSUMPTION))); }
            if (c.getColumnIndex(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE) != -1)  {vehicleGraphStatistics.setYaxis_avg_cost_litre(c.getInt(c.getColumnIndexOrThrow(VEHICLE_GRAPH_STATISTICS_YAXIS_AVG_COST_LITRE))); }
        }
        return vehicleGraphStatistics;
    }
}

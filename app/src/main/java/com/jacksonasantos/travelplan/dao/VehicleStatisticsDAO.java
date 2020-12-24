package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsISchema;

import java.util.ArrayList;
import java.util.List;

public class VehicleStatisticsDAO extends DbContentProvider implements VehicleStatisticsISchema, VehicleStatisticsIDAO {

    private Cursor cursor;

    public VehicleStatisticsDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<VehicleStatistics> findTotalVehicleStatistics(Integer vehicle_id) {
        List<VehicleStatistics> vehicleStatisticsList = new ArrayList<>();
        cursor = super.rawQuery("SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + ", " +
                        "9 supply_reason_type, " +
                        "(SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + ") ) avg_cost_litre, " +
                        "(SUM("+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS+") ) avg_consumption " +
                        "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                        "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
                        "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                        "GROUP BY " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID,
                new String[] { String.valueOf(vehicle_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    VehicleStatistics vehicleStatistics = cursorToEntity(cursor);
                    vehicleStatisticsList.add(vehicleStatistics);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehicleStatisticsList;
    }

    public List<VehicleStatistics> findLastVehicleStatistics(Integer vehicle_id) {
        List<VehicleStatistics> vehicleStatisticsList = new ArrayList<>();
        cursor = super.rawQuery("SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + ", " +
                        FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + ", " +
                        "(SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + ") ) avg_cost_litre, " +
                        "(SUM("+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS+") ) avg_consumption " +
                        "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                        "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
                        "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                        "GROUP BY " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + ", " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID,
                new String[] { String.valueOf(vehicle_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    VehicleStatistics vehicleStatistics = cursorToEntity(cursor);
                    vehicleStatisticsList.add(vehicleStatistics);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehicleStatisticsList;
    }

    protected VehicleStatistics cursorToEntity(Cursor cursor) {

        VehicleStatistics vehicleStatistics = new VehicleStatistics();

        int vehicle_idIndex;
        int supply_reason_typeIndex;
        int avg_consumptionIndex;
        int avg_cost_litreIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_VEHICLE_ID);
                vehicleStatistics.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE) != -1) {
                supply_reason_typeIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE);
                vehicleStatistics.setSupply_reason_type(cursor.getInt(supply_reason_typeIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_AVG_COST_LITRE) != -1) {
                avg_cost_litreIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_AVG_COST_LITRE);
                vehicleStatistics.setAvg_cost_litre(cursor.getFloat(avg_cost_litreIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_AVG_CONSUMPTION) != -1) {
                avg_consumptionIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_AVG_CONSUMPTION);
                vehicleStatistics.setAvg_consumption(cursor.getFloat(avg_consumptionIndex));
            }
        }
        return vehicleStatistics;
    }
}

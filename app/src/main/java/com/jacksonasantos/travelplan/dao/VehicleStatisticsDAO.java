package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

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
                                             "(AVG("+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+" / (" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS + "+" + FuelSupplyISchema.FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+")) ) avg_consumption " +
                                       "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                                      "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                                        "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
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
                                             " AVG( avg_cost_litre ) avg_cost_litre, " +
                                             " AVG( avg_consumption ) avg_consumption, " +
                                             " MAX( avg_consumption ) avg_max_consumption " +
                                      "FROM (SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + ", " +
                                                        FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + ", " +
                                                    "(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE + " / " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " ) avg_cost_litre, " +
                                                    "(" + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " / (" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS + "+" + FuelSupplyISchema.FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+ ") ) avg_consumption " +
                                              "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                                             "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                                               "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 ) " +
                                      "GROUP BY " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + ", " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID,
                new String[] { String.valueOf(vehicle_id) });
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

    public VehicleStatistics findLastVehicleStatistics(Integer vehicle_id, int reason_type) {
        VehicleStatistics vehicleStatisticsList = new VehicleStatistics();
        cursor = super.rawQuery("SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + " "+VEHICLE_STATISTICS_VEHICLE_ID+", " +
                                                 FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + " "+VEHICLE_STATISTICS_SUPPLY_REASON_TYPE+", " +
                                             " AVG( avg_cost_litre ) "+VEHICLE_STATISTICS_AVG_COST_LITRE+", " +
                                             " AVG( avg_consumption ) "+VEHICLE_STATISTICS_AVG_CONSUMPTION+", " +
                                             " MAX( avg_consumption ) "+VEHICLE_STATISTICS_AVG_MAX_CONSUMPTION+" " +
                                       "FROM (SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + ", " +
                                                         FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + ", " +
                                                     "(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE + " / " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " ) avg_cost_litre, " +
                                                     "(" + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " / (" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS + "+" + FuelSupplyISchema.FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+ ") ) avg_consumption " +
                                               "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                                              "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=?  " +
                                                "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
                                                "AND " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + "=? ) " +
                                      "GROUP BY " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID +
                                             ", " + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE,
                new String[] { String.valueOf(vehicle_id), String.valueOf(reason_type) });
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                vehicleStatisticsList = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return vehicleStatisticsList;
    }

    public List<VehicleStatistics> findLastVehicleGraphStatistics(Integer vehicle_id, Integer type) {
        List<VehicleStatistics> vehicleGraphStatisticsList = new ArrayList<>();
        cursor = super.rawQuery("SELECT " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + " " + VehicleStatisticsISchema.VEHICLE_STATISTICS_VEHICLE_ID+", " +
                                                FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_REASON_TYPE + " " + VehicleStatisticsISchema.VEHICLE_STATISTICS_SUPPLY_REASON_TYPE+ ", " +
                                                FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_DATE + " " + VehicleStatisticsISchema.VEHICLE_STATISTICS_STATISTIC_DATE+ ", " +
                                                "(SUM("+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS+"+"+FuelSupplyISchema.FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+") ) " + VehicleStatisticsISchema.VEHICLE_STATISTICS_AVG_CONSUMPTION+", " +
                                                "(SUM(" + FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_VALUE+") / SUM(" + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + ") ) "+ VehicleStatisticsISchema.VEHICLE_STATISTICS_AVG_COST_LITRE+" " +
                                       "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " " +
                                      "WHERE " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                                        "AND " + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
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
                    VehicleStatistics vehicleStatistics = cursorToEntity(cursor);
                    vehicleGraphStatisticsList.add(vehicleStatistics);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehicleGraphStatisticsList;
    }

    protected VehicleStatistics cursorToEntity(Cursor c) {
        VehicleStatistics vS = new VehicleStatistics();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_STATISTICS_VEHICLE_ID) != -1)          {vS.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE) != -1)  {vS.setSupply_reason_type(c.getInt(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_STATISTIC_DATE) != -1)      {vS.setStatistic_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_STATISTIC_DATE)))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_AVG_COST_LITRE) != -1)      {vS.setAvg_cost_litre(c.getFloat(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_AVG_COST_LITRE))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_AVG_CONSUMPTION) != -1)     {vS.setAvg_consumption(c.getFloat( c.getColumnIndexOrThrow(VEHICLE_STATISTICS_AVG_CONSUMPTION))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_AVG_MAX_CONSUMPTION) != -1) {vS.setAvg_max_consumption(c.getFloat( c.getColumnIndexOrThrow(VEHICLE_STATISTICS_AVG_MAX_CONSUMPTION))); }
        }
        return vS;
    }
}
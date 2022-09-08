package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.FuelSupplyISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsPeriodIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsPeriodISchema;

import java.util.ArrayList;
import java.util.List;

public class VehicleStatisticsPeriodDAO extends DbContentProvider implements VehicleStatisticsPeriodISchema, VehicleStatisticsPeriodIDAO {

    private Cursor cursor;

    public VehicleStatisticsPeriodDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<VehicleStatisticsPeriod> findVehicleStatisticsPeriod(Integer vehicle_id) {
        List<VehicleStatisticsPeriod> vehicleStatisticsPeriodList = new ArrayList<>();
        cursor = super.rawQuery("SELECT f." + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + " " + VehicleStatisticsPeriodISchema.VEHICLE_STATISTICS_PERIOD_VEHICLE_ID+", " +
                                            "v." + VehicleISchema.VEHICLE_SHORT_NAME + " " + VehicleStatisticsPeriodISchema.VEHICLE_STATISTICS_PERIOD_SHORT_NAME+ ", " +
                                            "STRFTIME( '%Y-%m',f."+FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_DATE+") " + VehicleStatisticsPeriodISchema.VEHICLE_STATISTICS_PERIOD_STATISTIC_PERIOD+ ", " +
                                            "(SUM(f."+ FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE+") / SUM(f." + FuelSupplyISchema.FUEL_SUPPLY_NUMBER_LITERS+"+f."+FuelSupplyISchema.FUEL_SUPPLY_ACCUMULATED_NUMBER_LITERS+") ) " + VehicleStatisticsPeriodISchema.VEHICLE_STATISTICS_PERIOD_AVG_CONSUMPTION+" " +
                                      "FROM " + FuelSupplyISchema.FUEL_SUPPLY_TABLE + " f " +
                                      "JOIN " + VehicleISchema.VEHICLE_TABLE + " v ON v." + VehicleISchema.VEHICLE_ID + " = f." + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + " " +
                                     "WHERE f." + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_TRAVELLED_DISTANCE + " > 0 " +
                                       "AND f." + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID + "=? " +
                                     "GROUP BY f." + FuelSupplyISchema.FUEL_SUPPLY_VEHICLE_ID +
                                            ", v." + VehicleISchema.VEHICLE_SHORT_NAME +
                                            ", STRFTIME( '%Y-%m',f."+FuelSupplyISchema.FUEL_SUPPLY_SUPPLY_DATE+")",
                new String[] { String.valueOf(vehicle_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    VehicleStatisticsPeriod vehicleStatisticsPeriod = cursorToEntity(cursor);
                    vehicleStatisticsPeriodList.add(vehicleStatisticsPeriod);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehicleStatisticsPeriodList;
    }

    protected VehicleStatisticsPeriod cursorToEntity(Cursor c) {
        VehicleStatisticsPeriod vSP = new VehicleStatisticsPeriod();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_STATISTICS_PERIOD_VEHICLE_ID) != -1)       {vSP.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_PERIOD_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_PERIOD_SHORT_NAME) != -1)       {vSP.setShort_name(c.getString(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_PERIOD_SHORT_NAME))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_PERIOD_STATISTIC_PERIOD) != -1) {vSP.setStatistic_period(c.getString(c.getColumnIndexOrThrow(VEHICLE_STATISTICS_PERIOD_STATISTIC_PERIOD))); }
            if (c.getColumnIndex(VEHICLE_STATISTICS_PERIOD_AVG_CONSUMPTION) != -1)  {vSP.setAvg_consumption(c.getFloat( c.getColumnIndexOrThrow(VEHICLE_STATISTICS_PERIOD_AVG_CONSUMPTION))); }
        }
        return vSP;
    }
}
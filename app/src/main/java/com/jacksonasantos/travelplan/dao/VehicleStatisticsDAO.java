package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleStatisticsISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VehicleStatisticsDAO extends DbContentProvider implements VehicleStatisticsISchema, VehicleStatisticsIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public VehicleStatisticsDAO(SQLiteDatabase db) {
        super(db);
    }

    public VehicleStatistics fetchVehicleStatisticsById(Long id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_STATISTICS_ID + " = ?";
        VehicleStatistics vehicleStatistics = new VehicleStatistics();
        try {
            cursor = super.query(VEHICLE_STATISTICS_TABLE, VEHICLE_STATISTICS_COLUMNS, selection, selectionArgs, VEHICLE_STATISTICS_ID);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    vehicleStatistics = cursorToEntity(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (SQLiteConstraintException ex) {
            Log.w("Update Table", ex.getMessage());
        }
        return vehicleStatistics;
    }

    public List<VehicleStatistics> fetchAllVehicleStatistics() {
        List<VehicleStatistics> vehicleStatisticsList = new ArrayList<>();

        if (Globals.getInstance().getFilterVehicle()) {
            final String[] selectionArgs = { String.valueOf(Globals.getInstance().getIdVehicle()) };
            final String selection = VEHICLE_STATISTICS_ID + " = ?";

            cursor = super.query(VEHICLE_STATISTICS_TABLE, VEHICLE_STATISTICS_COLUMNS, selection, selectionArgs, VEHICLE_STATISTICS_ID);
        } else {
            cursor = super.query(VEHICLE_STATISTICS_TABLE, VEHICLE_STATISTICS_COLUMNS, null, null, VEHICLE_STATISTICS_ID);
        }

        if (cursor.moveToFirst()) {
            do {
                VehicleStatistics vehicle = cursorToEntity(cursor);
                vehicleStatisticsList.add(vehicle);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return vehicleStatisticsList;
    }

    public List<VehicleStatistics> findVehicleStatisticsbyId(Long id) {
        List<VehicleStatistics> vehicleStatisticsList = new ArrayList<>();

        cursor = super.rawQuery(
                "SELECT * " +
                     "FROM " + VEHICLE_STATISTICS_TABLE + " a, " +
                     "(SELECT " +VEHICLE_STATISTICS_SUPPLY_REASON_TYPE +" supply_reason_type, "+
                              "MAX("+VEHICLE_STATISTICS_STATISTIC_DATE+") statistic_date "+
                        "FROM " + VEHICLE_STATISTICS_TABLE +
                        " WHERE " + VEHICLE_STATISTICS_VEHICLE_ID + " = ? "+
                        "GROUP BY " + VEHICLE_STATISTICS_SUPPLY_REASON_TYPE + ") b " +
                     "WHERE a.supply_reason_type = b.supply_reason_type "+
                     "AND a.statistic_date = b.statistic_date ",
                new String[] { String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                VehicleStatistics vehicle = cursorToEntity(cursor);
                vehicleStatisticsList.add(vehicle);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return vehicleStatisticsList;
    }

    public void deleteVehicleStatistics(int vehicleStatisticsId) {
        final String[] selectionArgs = { String.valueOf(vehicleStatisticsId) };
        final String selection = VEHICLE_STATISTICS_ID + " = ?";
        try {
            super.delete(VEHICLE_STATISTICS_TABLE, selection, selectionArgs);
        } catch (SQLiteConstraintException ex){
            Log.w("Delete Table", ex.getMessage());
        }
    }

    public boolean updateVehicleStatistics(VehicleStatistics vehicleStatistics) {
        setContentValue(vehicleStatistics);
        final String[] selectionArgs = { String.valueOf(vehicleStatistics.getId()) };
        final String selection = VEHICLE_STATISTICS_ID + " = ?";
        try {
            return super.update(VEHICLE_STATISTICS_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addVehicleStatistics(VehicleStatistics vehicleStatistics) {
        setContentValue(vehicleStatistics);
        try {
            return super.insert(VEHICLE_STATISTICS_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", ex.getMessage());
            return false;
        }
    }

    protected VehicleStatistics cursorToEntity(Cursor cursor) {

        VehicleStatistics vehicleStatistics = new VehicleStatistics();

        int idIndex;
        int vehicle_idIndex;
        int statistic_dateIndex;
        int supply_reason_typeIndex;
        int avg_consumptionIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_ID);
                vehicleStatistics.id = cursor.getLong(idIndex);
            }
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_VEHICLE_ID);
                vehicleStatistics.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_STATISTIC_DATE) != -1) {
                statistic_dateIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_STATISTIC_DATE);
                vehicleStatistics.setStatistic_date(Utils.stringToDate(cursor.getString(statistic_dateIndex)));
            }

            if (cursor.getColumnIndex(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE) != -1) {
                supply_reason_typeIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE);
                vehicleStatistics.setSupply_reason_type(cursor.getInt(supply_reason_typeIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_STATISTICS_AVG_CONSUMPTION) != -1) {
                avg_consumptionIndex = cursor.getColumnIndexOrThrow(VEHICLE_STATISTICS_AVG_CONSUMPTION);
                vehicleStatistics.setAvg_consumption(cursor.getFloat(avg_consumptionIndex));
            }
        }
        return vehicleStatistics;
    }

    private void setContentValue(VehicleStatistics vehicleStatistics) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_STATISTICS_VEHICLE_ID, vehicleStatistics.vehicle_id);
        initialValues.put(VEHICLE_STATISTICS_STATISTIC_DATE, Utils.dateToString(vehicleStatistics.statistic_date));
        initialValues.put(VEHICLE_STATISTICS_SUPPLY_REASON_TYPE, vehicleStatistics.supply_reason_type);
        initialValues.put(VEHICLE_STATISTICS_AVG_CONSUMPTION, vehicleStatistics.avg_consumption);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

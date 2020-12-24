package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasPlanIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasPlanISchema;

import java.util.ArrayList;
import java.util.List;

public class VehicleHasPlanDAO extends DbContentProvider implements VehicleHasPlanISchema, VehicleHasPlanIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public VehicleHasPlanDAO(SQLiteDatabase db) {
        super(db);
    }

    public VehicleHasPlan fetchVehicleHasPlanById(Integer vehicle_id, Integer maintenance_id) {
        final String[] selectionArgs = { String.valueOf(vehicle_id),String.valueOf(maintenance_id) };
        final String selection = VEHICLE_HAS_PLAN_VEHICLE_ID + " = ? AND " + 
                                 VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " = ? ";
        VehicleHasPlan vehicleHasPlan = new VehicleHasPlan();
        cursor = super.query(VEHICLE_HAS_PLAN_TABLE, VEHICLE_HAS_PLAN_COLUMNS, selection, selectionArgs, VEHICLE_HAS_PLAN_VEHICLE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                vehicleHasPlan = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return vehicleHasPlan;
    }

    public List<VehicleHasPlan> fetchAllVehicleHasPlan() {
        List<VehicleHasPlan> vehicleHasPlanList = new ArrayList<>();
        cursor = super.query(VEHICLE_HAS_PLAN_TABLE, VEHICLE_HAS_PLAN_COLUMNS, null, null, VEHICLE_HAS_PLAN_VEHICLE_ID);
        if (cursor.moveToFirst()) {
            do {
                VehicleHasPlan vehicleHasPlan = cursorToEntity(cursor);
                vehicleHasPlanList.add(vehicleHasPlan);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return vehicleHasPlanList;
    }
    public List<VehicleHasPlan> fetchAllVehicleHasPlanByVehicle(Integer vehicle_id) {
        final String[] selectionArgs = { String.valueOf(vehicle_id) };
        final String selection = VEHICLE_HAS_PLAN_VEHICLE_ID + " = ?";
        List<VehicleHasPlan> vehicleHasPlanList = new ArrayList<>();
        cursor = super.query(VEHICLE_HAS_PLAN_TABLE, VEHICLE_HAS_PLAN_COLUMNS, selection, selectionArgs, VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID);
        if (cursor.moveToFirst()) {
            do {
                VehicleHasPlan vehicleHasPlan = cursorToEntity(cursor);
                vehicleHasPlanList.add(vehicleHasPlan);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return vehicleHasPlanList;
    }

    public void deleteVehicleHasPlan(Integer vehicle_id, Integer maintenance_id) {
        final String[] selectionArgs = { String.valueOf(vehicle_id),String.valueOf(maintenance_id) };
        final String selection = VEHICLE_HAS_PLAN_VEHICLE_ID + " = ? AND " +
                VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " = ? ";
        super.delete(VEHICLE_HAS_PLAN_TABLE, selection, selectionArgs);
    }

    public boolean updateVehicleHasPlan(VehicleHasPlan vehicleHasPlan) {
        setContentValue(vehicleHasPlan);
        final String[] selectionArgs = { String.valueOf(vehicleHasPlan.getVehicle_id()),String.valueOf(vehicleHasPlan.getMaintenance_plan_id()) };
        final String selection = VEHICLE_HAS_PLAN_VEHICLE_ID + " = ? AND " +
                VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " = ? ";
        return (super.update(VEHICLE_HAS_PLAN_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addVehicleHasPlan(VehicleHasPlan vehicleHasPlan) {
        setContentValue(vehicleHasPlan);
        return (super.insert(VEHICLE_HAS_PLAN_TABLE, getContentValue()) > 0);
    }

    protected VehicleHasPlan cursorToEntity(Cursor cursor) {

        VehicleHasPlan vehicleHasPlan = new VehicleHasPlan();

        int vehicle_idIndex;
        int maintenance_idIndex;
        int expirationIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_HAS_PLAN_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_VEHICLE_ID);
                vehicleHasPlan.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID) != -1) {
                maintenance_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID);
                vehicleHasPlan.setMaintenance_plan_id(cursor.getInt(maintenance_idIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_HAS_PLAN_EXPIRATION) != -1) {
                expirationIndex = cursor.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_EXPIRATION);
                vehicleHasPlan.setExpiration(cursor.getInt(expirationIndex));
            }
        }
        return vehicleHasPlan;
    }

    private void setContentValue(VehicleHasPlan vehicleHasPlan) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_HAS_PLAN_VEHICLE_ID, vehicleHasPlan.vehicle_id);
        initialValues.put(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID, vehicleHasPlan.maintenance_plan_id);
        initialValues.put(VEHICLE_HAS_PLAN_EXPIRATION, vehicleHasPlan.expiration);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

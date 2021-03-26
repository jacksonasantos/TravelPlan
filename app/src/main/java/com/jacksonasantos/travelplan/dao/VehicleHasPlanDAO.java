package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
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

    public VehicleHasPlan fetchVehicleHasPlanById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_HAS_PLAN_ID + " = ? ";
        VehicleHasPlan vehicleHasPlan = new VehicleHasPlan();
        cursor = super.query(VEHICLE_HAS_PLAN_TABLE, VEHICLE_HAS_PLAN_COLUMNS, selection, selectionArgs, null);
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

    public VehicleHasPlan fetchVehicleHasPlanByFK(Integer vehicle_id, Integer maintenance_plan_id) {
        final String[] selectionArgs = { String.valueOf(vehicle_id), String.valueOf(maintenance_plan_id) };
        final String selection = VEHICLE_HAS_PLAN_VEHICLE_ID + " = ? AND "+
                                 VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " = ?";
        VehicleHasPlan vehicleHasPlan = new VehicleHasPlan();
        cursor = super.query(VEHICLE_HAS_PLAN_TABLE, VEHICLE_HAS_PLAN_COLUMNS, selection, selectionArgs, null);
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

    public void deleteVehicleHasPlan(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_HAS_PLAN_ID + " = ? ";
        super.delete(VEHICLE_HAS_PLAN_TABLE, selection, selectionArgs);
    }

    public boolean updateVehicleHasPlan(VehicleHasPlan vehicleHasPlan) {
        setContentValue(vehicleHasPlan);
        final String[] selectionArgs = { String.valueOf(vehicleHasPlan.getId()) };
        final String selection = VEHICLE_HAS_PLAN_ID + " = ? ";
        return (super.update(VEHICLE_HAS_PLAN_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addVehicleHasPlan(VehicleHasPlan vehicleHasPlan) {
        setContentValue(vehicleHasPlan);
        return (super.insert(VEHICLE_HAS_PLAN_TABLE, getContentValue()) > 0);
    }

    protected VehicleHasPlan cursorToEntity(Cursor c) {
        VehicleHasPlan vHP = new VehicleHasPlan();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_ID) != -1)                   {vHP.setId(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_VEHICLE_ID) != -1)           {vHP.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID) != -1)  {vHP.setMaintenance_plan_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_EXPIRATION) != -1)           {vHP.setExpiration(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_EXPIRATION))); }
        }
        return vHP;
    }

    private void setContentValue(VehicleHasPlan vHP) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_HAS_PLAN_ID, vHP.id);
        initialValues.put(VEHICLE_HAS_PLAN_VEHICLE_ID, vHP.vehicle_id);
        initialValues.put(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID, vHP.maintenance_plan_id);
        initialValues.put(VEHICLE_HAS_PLAN_EXPIRATION, vHP.expiration);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

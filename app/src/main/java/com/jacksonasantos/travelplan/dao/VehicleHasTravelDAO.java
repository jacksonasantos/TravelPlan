package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasTravelIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasTravelISchema;

import java.util.ArrayList;
import java.util.List;

public class VehicleHasTravelDAO extends DbContentProvider implements VehicleHasTravelISchema, VehicleHasTravelIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public VehicleHasTravelDAO(SQLiteDatabase db) {
        super(db);
    }

    public VehicleHasTravel fetchVehicleHasTravelById(Integer vehicle_id, Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(vehicle_id),String.valueOf(travel_id) };
        final String selection = VEHICLE_HAS_TRAVEL_VEHICLE_ID + " = ? AND " + 
                                 VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = ? ";
        VehicleHasTravel vehicleHasTravel = new VehicleHasTravel();
        cursor = super.query(VEHICLE_HAS_TRAVEL_TABLE, VEHICLE_HAS_TRAVEL_COLUMNS, selection, selectionArgs, VEHICLE_HAS_TRAVEL_VEHICLE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                vehicleHasTravel = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return vehicleHasTravel;
    }

    public List<VehicleHasTravel> fetchAllVehicleHasTravel() {
        List<VehicleHasTravel> vehicleHasTravelList = new ArrayList<>();
        cursor = super.query(VEHICLE_HAS_TRAVEL_TABLE, VEHICLE_HAS_TRAVEL_COLUMNS, null, null, VEHICLE_HAS_TRAVEL_VEHICLE_ID);
        if (cursor.moveToFirst()) {
            do {
                VehicleHasTravel vehicleHasTravel = cursorToEntity(cursor);
                vehicleHasTravelList.add(vehicleHasTravel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return vehicleHasTravelList;
    }
    public List<VehicleHasTravel> fetchAllVehicleHasTravelByTravel(Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = ?";
        List<VehicleHasTravel> vehicleHasTravelList = new ArrayList<>();
        cursor = super.query(VEHICLE_HAS_TRAVEL_TABLE, VEHICLE_HAS_TRAVEL_COLUMNS, selection, selectionArgs, VEHICLE_HAS_TRAVEL_VEHICLE_ID);
        if (cursor.moveToFirst()) {
            do {
                VehicleHasTravel vehicleHasTravel = cursorToEntity(cursor);
                vehicleHasTravelList.add(vehicleHasTravel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return vehicleHasTravelList;
    }

    public void deleteVehicleHasTravel(Integer vehicle_id, Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(vehicle_id),String.valueOf(travel_id) };
        final String selection = VEHICLE_HAS_TRAVEL_VEHICLE_ID + " = ? AND " +
                VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = ? ";
        super.delete(VEHICLE_HAS_TRAVEL_TABLE, selection, selectionArgs);
    }

    public boolean updateVehicleHasTravel(VehicleHasTravel vehicleHasTravel) {
        setContentValue(vehicleHasTravel);
        final String[] selectionArgs = { String.valueOf(vehicleHasTravel.getVehicle_id()),String.valueOf(vehicleHasTravel.getTravel_id()) };
        final String selection = VEHICLE_HAS_TRAVEL_VEHICLE_ID + " = ? AND " +
                VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = ? ";
        return (super.update(VEHICLE_HAS_TRAVEL_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addVehicleHasTravel(VehicleHasTravel vehicleHasTravel) {
        setContentValue(vehicleHasTravel);
        return (super.insert(VEHICLE_HAS_TRAVEL_TABLE, getContentValue()) > 0);
    }

    protected VehicleHasTravel cursorToEntity(Cursor cursor) {

        VehicleHasTravel vehicleHasTravel = new VehicleHasTravel();

        int vehicle_idIndex;
        int travel_idIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(VEHICLE_HAS_TRAVEL_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_VEHICLE_ID);
                vehicleHasTravel.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(VEHICLE_HAS_TRAVEL_TRAVEL_ID) != -1) {
                travel_idIndex = cursor.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_TRAVEL_ID);
                vehicleHasTravel.setTravel_id(cursor.getInt(travel_idIndex));
            }
        }
        return vehicleHasTravel;
    }

    private void setContentValue(VehicleHasTravel vehicleHasTravel) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_HAS_TRAVEL_VEHICLE_ID, vehicleHasTravel.vehicle_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_TRAVEL_ID, vehicleHasTravel.travel_id);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

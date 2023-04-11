package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
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

    protected VehicleHasTravel cursorToEntity(Cursor c) {
        VehicleHasTravel vHT = new VehicleHasTravel();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_VEHICLE_ID) != -1)      {vHT.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_TRAVEL_ID) != -1)       {vHT.setTravel_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_TRAVEL_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_PERSON_ID) != -1)       {vHT.setPerson_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_PERSON_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_START_ODOMETER) != -1)  {vHT.setStart_odometer(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_START_ODOMETER))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_FINAL_ODOMETER) != -1)  {vHT.setFinal_odometer(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_FINAL_ODOMETER))); }
        }
        return vHT;
    }

    private void setContentValue(VehicleHasTravel vHT) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_HAS_TRAVEL_VEHICLE_ID, vHT.vehicle_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_TRAVEL_ID, vHT.travel_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_PERSON_ID, vHT.person_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_START_ODOMETER, vHT.start_odometer);
        initialValues.put(VEHICLE_HAS_TRAVEL_FINAL_ODOMETER, vHT.final_odometer);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

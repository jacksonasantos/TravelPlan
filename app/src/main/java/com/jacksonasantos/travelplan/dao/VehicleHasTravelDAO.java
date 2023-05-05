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

    public VehicleHasTravel fetchAllVehicleHasTravelById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = VEHICLE_HAS_TRAVEL_ID + " = ? ";
        VehicleHasTravel vehicleHasTravel = new VehicleHasTravel();
        cursor = super.query(VEHICLE_HAS_TRAVEL_TABLE, VEHICLE_HAS_TRAVEL_COLUMNS, selection, selectionArgs, null);
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

    public VehicleHasTravel findVehicleHasTravelByTravelVehicle(Integer travel_id, Integer vehicle_id) {
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(vehicle_id) };
        final String selection = VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = ? AND "+VEHICLE_HAS_TRAVEL_VEHICLE_ID + " = ? ";
        VehicleHasTravel vehicleHasTravel = new VehicleHasTravel();
        cursor = super.query(VEHICLE_HAS_TRAVEL_TABLE, VEHICLE_HAS_TRAVEL_COLUMNS, selection, selectionArgs, null);
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
    public VehicleHasTravel findVehicleHasTravelByTravelTransport(Integer travel_id, Integer transport_id) {
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(transport_id) };
        final String selection = VEHICLE_HAS_TRAVEL_TRAVEL_ID + " = ? AND "+VEHICLE_HAS_TRAVEL_TRANSPORT_ID + " = ? ";
        VehicleHasTravel vehicleHasTravel = new VehicleHasTravel();
        cursor = super.query(VEHICLE_HAS_TRAVEL_TABLE, VEHICLE_HAS_TRAVEL_COLUMNS, selection, selectionArgs, null);
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

    public void deleteVehicleHasTravel(Integer id) {
        final String[] selectionArgs = { String.valueOf(id)};
        final String selection = VEHICLE_HAS_TRAVEL_ID + " = ? ";
        super.delete(VEHICLE_HAS_TRAVEL_TABLE, selection, selectionArgs);
    }

    public boolean updateVehicleHasTravel(VehicleHasTravel vehicleHasTravel) {
        setContentValue(vehicleHasTravel);
        final String[] selectionArgs = { String.valueOf(vehicleHasTravel.getId())};
        final String selection = VEHICLE_HAS_TRAVEL_ID + " = ? ";
        return (super.update(VEHICLE_HAS_TRAVEL_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addVehicleHasTravel(VehicleHasTravel vehicleHasTravel) {
        setContentValue(vehicleHasTravel);
        return (super.insert(VEHICLE_HAS_TRAVEL_TABLE, getContentValue()) > 0);
    }

    protected VehicleHasTravel cursorToEntity(Cursor c) {
        VehicleHasTravel vHT = new VehicleHasTravel();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_ID) != -1)              {vHT.setId(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_VEHICLE_ID) != -1)      {vHT.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_TRAVEL_ID) != -1)       {vHT.setTravel_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_TRAVEL_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_TRANSPORT_ID) != -1)    {vHT.setTransport_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_TRANSPORT_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_PERSON_ID) != -1)       {vHT.setPerson_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_PERSON_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_START_ODOMETER) != -1)  {vHT.setStart_odometer(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_START_ODOMETER))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_FINAL_ODOMETER) != -1)  {vHT.setFinal_odometer(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_FINAL_ODOMETER))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_AVG_CONSUMPTION) != -1) {vHT.setAvg_consumption(c.getFloat(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_AVG_CONSUMPTION))); }
            if (c.getColumnIndex(VEHICLE_HAS_TRAVEL_AVG_COST_LITRE) != -1)  {vHT.setAvg_cost_litre(c.getFloat(c.getColumnIndexOrThrow(VEHICLE_HAS_TRAVEL_AVG_COST_LITRE))); }
        }
        return vHT;
    }

    private void setContentValue(VehicleHasTravel vHT) {
        initialValues = new ContentValues();
        initialValues.put(VEHICLE_HAS_TRAVEL_ID, vHT.id);
        initialValues.put(VEHICLE_HAS_TRAVEL_VEHICLE_ID, vHT.vehicle_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_TRAVEL_ID, vHT.travel_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_TRANSPORT_ID, vHT.transport_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_PERSON_ID, vHT.person_id);
        initialValues.put(VEHICLE_HAS_TRAVEL_START_ODOMETER, vHT.start_odometer);
        initialValues.put(VEHICLE_HAS_TRAVEL_FINAL_ODOMETER, vHT.final_odometer);
        initialValues.put(VEHICLE_HAS_TRAVEL_AVG_CONSUMPTION, vHT.avg_consumption);
        initialValues.put(VEHICLE_HAS_TRAVEL_AVG_COST_LITRE, vHT.avg_cost_litre);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.PendingVehicleIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.PendingVehicleISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.ArrayList;
import java.util.List;

public class PendingVehicleDAO extends DbContentProvider implements PendingVehicleISchema, PendingVehicleIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public PendingVehicleDAO(SQLiteDatabase db) {
        super(db);
    }

    public ArrayList<PendingVehicle> fetchArrayPendingVehicle(Integer vehicle_id){
        final String[] selectionArgs = { String.valueOf(vehicle_id) };
        final String selection = PENDING_VEHICLE_VEHICLE_ID + " = ?";
        ArrayList<PendingVehicle> pendingVehicle = new ArrayList<>();
        Cursor cursor = super.query(PENDING_VEHICLE_TABLE, PENDING_VEHICLE_COLUMNS, selection,selectionArgs, PENDING_VEHICLE_SERVICE_TYPE);
        if(cursor != null && cursor.moveToFirst()){
            do{
                PendingVehicle pV = cursorToEntity(cursor);
                pendingVehicle.add(pV);
            }while(cursor.moveToNext());
        }
        return pendingVehicle;
    }

    public PendingVehicle fetchPendingVehicleById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = PENDING_VEHICLE_ID + " = ?";
        PendingVehicle pendingVehicle = new PendingVehicle();
        cursor = super.query(PENDING_VEHICLE_TABLE, PENDING_VEHICLE_COLUMNS, selection, selectionArgs, PENDING_VEHICLE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                pendingVehicle = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return pendingVehicle;
    }

    public PendingVehicle fetchPendingVehicleByService(int type) {
        final String[] selectionArgs = { String.valueOf(type) };
        final String selection = PENDING_VEHICLE_SERVICE_TYPE + " = ? ";
        PendingVehicle pendingVehicle = new PendingVehicle();
        cursor = super.query(PENDING_VEHICLE_TABLE, PENDING_VEHICLE_COLUMNS, selection, selectionArgs, PENDING_VEHICLE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                pendingVehicle = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return pendingVehicle;
    }

    public List<PendingVehicle> fetchAllPendingVehicle() {
        List<PendingVehicle> pendingVehicleList = new ArrayList<>();

        if (Globals.getInstance().getFilterVehicle()) {
            final String[] selectionArgs = { String.valueOf(Globals.getInstance().getIdVehicle()) };
            final String selection = PENDING_VEHICLE_VEHICLE_ID + " = ?";

            cursor = super.query(PENDING_VEHICLE_TABLE, PENDING_VEHICLE_COLUMNS, selection, selectionArgs, PENDING_VEHICLE_VEHICLE_ID+","+PENDING_VEHICLE_SERVICE_TYPE);
        } else {
            cursor = super.query(PENDING_VEHICLE_TABLE, PENDING_VEHICLE_COLUMNS, null, null, PENDING_VEHICLE_VEHICLE_ID+","+PENDING_VEHICLE_SERVICE_TYPE);
        }

        if (cursor.moveToFirst()) {
            do {
                PendingVehicle pendingVehicle = cursorToEntity(cursor);
                pendingVehicleList.add(pendingVehicle);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return pendingVehicleList;
    }

    public List<PendingVehicle> fetchAllPendingVehicle(Integer vehicle_id, int status) {
        List<PendingVehicle> pendingVehicleList = new ArrayList<>();
        final String[] selectionArgs = { String.valueOf(vehicle_id), String.valueOf(status)};
        final String selection = PENDING_VEHICLE_VEHICLE_ID + " = ? AND " + PENDING_VEHICLE_STATUS_PENDING + " = ?";

        cursor = super.query(PENDING_VEHICLE_TABLE, PENDING_VEHICLE_COLUMNS, selection, selectionArgs, PENDING_VEHICLE_SERVICE_TYPE);
        if (cursor.moveToFirst()) {
            do {
                PendingVehicle pendingVehicle = cursorToEntity(cursor);
                pendingVehicleList.add(pendingVehicle);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return pendingVehicleList;
    }

    public void deletePendingVehicle(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = PENDING_VEHICLE_ID + " = ?";
        super.delete(PENDING_VEHICLE_TABLE, selection, selectionArgs);
    }

    public boolean updatePendingVehicle(PendingVehicle pendingVehicle) {
        setContentValue(pendingVehicle);
        final String[] selectionArgs = { String.valueOf(pendingVehicle.getId()) };
        final String selection = PENDING_VEHICLE_ID + " = ?";
        return (super.update(PENDING_VEHICLE_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addPendingVehicle(PendingVehicle pendingVehicle) {
        setContentValue(pendingVehicle);
        return (super.insert(PENDING_VEHICLE_TABLE, getContentValue()) > 0);
    }

    protected PendingVehicle cursorToEntity(Cursor c) {
        PendingVehicle mP = new PendingVehicle();
        if (c != null) {
            if (c.getColumnIndex(PENDING_VEHICLE_ID) != -1)                 {mP.setId(c.getInt(c.getColumnIndexOrThrow(PENDING_VEHICLE_ID))); }
            if (c.getColumnIndex(PENDING_VEHICLE_VEHICLE_ID) != -1)         {mP.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(PENDING_VEHICLE_VEHICLE_ID))); }
            if (c.getColumnIndex(PENDING_VEHICLE_SERVICE_TYPE) != -1)       {mP.setService_type(c.getInt(c.getColumnIndexOrThrow(PENDING_VEHICLE_SERVICE_TYPE))); }
            if (c.getColumnIndex(PENDING_VEHICLE_NOTE) != -1)               {mP.setNote(c.getString(c.getColumnIndexOrThrow(PENDING_VEHICLE_NOTE))); }
            if (c.getColumnIndex(PENDING_VEHICLE_EXPECTED_VALUE) != -1)     {mP.setExpected_value(c.getDouble(c.getColumnIndexOrThrow(PENDING_VEHICLE_EXPECTED_VALUE))); }
            if (c.getColumnIndex(PENDING_VEHICLE_STATUS_PENDING) != -1)     {mP.setStatus_pending(c.getInt(c.getColumnIndexOrThrow(PENDING_VEHICLE_STATUS_PENDING))); }
        }
        return mP;
    }

    private void setContentValue(PendingVehicle pV) {
        initialValues = new ContentValues();
        initialValues.put(PENDING_VEHICLE_ID, pV.id);
        initialValues.put(PENDING_VEHICLE_VEHICLE_ID, pV.vehicle_id);
        initialValues.put(PENDING_VEHICLE_SERVICE_TYPE, pV.service_type);
        initialValues.put(PENDING_VEHICLE_NOTE, pV.note);
        initialValues.put(PENDING_VEHICLE_EXPECTED_VALUE, pV.expected_value);
        initialValues.put(PENDING_VEHICLE_STATUS_PENDING, pV.status_pending);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

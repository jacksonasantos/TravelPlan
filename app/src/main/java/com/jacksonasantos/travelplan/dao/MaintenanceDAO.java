package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.ui.utility.Globals;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAO extends DbContentProvider implements MaintenanceISchema, MaintenanceIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public MaintenanceDAO(SQLiteDatabase db) {
        super(db);
    }

    public Maintenance fetchMaintenanceById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MAINTENANCE_ID + " = ?";
        Maintenance maintenance = new Maintenance();
        cursor = super.query(MAINTENANCE_TABLE, MAINTENANCE_COLUMNS, selection, selectionArgs, MAINTENANCE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                maintenance = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return maintenance;
    }

    public List<Maintenance> findReminderMaintenance( Integer id) {
        List<Maintenance> maintenanceList = new ArrayList<>();

        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MAINTENANCE_VEHICLE_ID + " = ? AND " +
                MAINTENANCE_STATUS + " = 0 "; //AND ( " +
    //            MAINTENANCE_EXPIRATION_DATE + " NOT NULL AND " +
    //            MAINTENANCE_EXPIRATION_DATE + " > DATE ('now') )" ;

        cursor = super.query(MAINTENANCE_TABLE, MAINTENANCE_COLUMNS, selection, selectionArgs, MAINTENANCE_EXPIRATION_DATE);

        if (cursor.moveToFirst()) {
            do {
                Maintenance maintenance = cursorToEntity(cursor);
                maintenanceList.add(maintenance);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return maintenanceList;
    }

    public List<Maintenance> fetchAllMaintenance() {
        List<Maintenance> maintenanceList = new ArrayList<>();

        if (Globals.getInstance().getFilterVehicle()) {
            final String[] selectionArgs = { String.valueOf(Globals.getInstance().getIdVehicle()) };
            final String selection = MAINTENANCE_VEHICLE_ID + " = ?";

            cursor = super.query(MAINTENANCE_TABLE, MAINTENANCE_COLUMNS, selection, selectionArgs, MAINTENANCE_DATE);
        } else {
            cursor = super.query(MAINTENANCE_TABLE, MAINTENANCE_COLUMNS, null, null, MAINTENANCE_DATE);
        }

        if (cursor.moveToFirst()) {
            do {
                Maintenance maintenance = cursorToEntity(cursor);
                maintenanceList.add(maintenance);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return maintenanceList;
    }

    public void deleteMaintenance(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MAINTENANCE_ID + " = ?";
        super.delete(MAINTENANCE_TABLE, selection, selectionArgs);
    }

    public boolean updateMaintenance(Maintenance maintenance) {
        setContentValue(maintenance);
        final String[] selectionArgs = { String.valueOf(maintenance.getId()) };
        final String selection = MAINTENANCE_ID + " = ?";
        return (super.update(MAINTENANCE_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addMaintenance(Maintenance maintenance) {
        setContentValue(maintenance);
        return (super.insert(MAINTENANCE_TABLE, getContentValue()) > 0);
    }

    protected Maintenance cursorToEntity(Cursor c) {
        Maintenance m = new Maintenance();
        if (c != null) {
            if (c.getColumnIndex(MAINTENANCE_ID) != -1)              {m.setId(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ID))); }
            if (c.getColumnIndex(MAINTENANCE_VEHICLE_ID) != -1)      {m.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_VEHICLE_ID))); }
            if (c.getColumnIndex(MAINTENANCE_SERVICE_TYPE) != -1)    {m.setService_type(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_SERVICE_TYPE))); }
            if (c.getColumnIndex(MAINTENANCE_DETAIL) != -1)          {m.setDetail(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_DETAIL))); }
            if (c.getColumnIndex(MAINTENANCE_DATE) != -1)            {m.setDate(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_DATE)))); }
            if (c.getColumnIndex(MAINTENANCE_EXPIRATION_DATE) != -1) {m.setExpiration_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_EXPIRATION_DATE)))); }
            if (c.getColumnIndex(MAINTENANCE_EXPIRATION_KM) != -1)   {m.setExpiration_km(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_EXPIRATION_KM))); }
            if (c.getColumnIndex(MAINTENANCE_ODOMETER) != -1)        {m.setOdometer(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ODOMETER))); }
            if (c.getColumnIndex(MAINTENANCE_VALUE) != -1)           {m.setValue(c.getDouble(c.getColumnIndexOrThrow(MAINTENANCE_VALUE))); }
            if (c.getColumnIndex(MAINTENANCE_LOCATION) != -1)        {m.setLocation(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_LOCATION))); }
            if (c.getColumnIndex(MAINTENANCE_NOTE) != -1)            {m.setNote(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_NOTE))); }
            if (c.getColumnIndex(MAINTENANCE_STATUS) != -1)          {m.setStatus(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_STATUS))); }
        }
        return m;
    }

    private void setContentValue(Maintenance m) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_ID, m.id);
        initialValues.put(MAINTENANCE_VEHICLE_ID, m.vehicle_id);
        initialValues.put(MAINTENANCE_SERVICE_TYPE, m.service_type);
        initialValues.put(MAINTENANCE_DETAIL, m.detail);
        initialValues.put(MAINTENANCE_DATE, Utils.dateFormat(m.date));
        initialValues.put(MAINTENANCE_EXPIRATION_DATE, Utils.dateFormat(m.expiration_date));
        initialValues.put(MAINTENANCE_EXPIRATION_KM, m.expiration_km);
        initialValues.put(MAINTENANCE_ODOMETER, m.odometer);
        initialValues.put(MAINTENANCE_VALUE, m.value);
        initialValues.put(MAINTENANCE_LOCATION, m.location);
        initialValues.put(MAINTENANCE_NOTE, m.note);
        initialValues.put(MAINTENANCE_STATUS, m.status);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

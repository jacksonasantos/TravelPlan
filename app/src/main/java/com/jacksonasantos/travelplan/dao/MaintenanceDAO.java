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

    protected Maintenance cursorToEntity(Cursor cursor) {

        Maintenance maintenance = new Maintenance();

        int idIndex;
        int vehicle_idIndex;
        int service_typeIndex;
        int detailIndex;
        int dateIndex;
        int expiration_dateIndex;
        int expiration_kmIndex;
        int odometerIndex;
        int valueIndex;
        int locationIndex;
        int noteIndex;
        int statusIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(MAINTENANCE_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_ID);
                maintenance.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_VEHICLE_ID);
                maintenance.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_SERVICE_TYPE) != -1) {
                service_typeIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_SERVICE_TYPE);
                maintenance.setService_type(cursor.getInt(service_typeIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_DETAIL) != -1) {
                detailIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_DETAIL);
                maintenance.setDetail(cursor.getString(detailIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_DATE) != -1) {
                dateIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_DATE);
                maintenance.setDate(Utils.dateParse(cursor.getString(dateIndex)));
            }
            if (cursor.getColumnIndex(MAINTENANCE_EXPIRATION_DATE) != -1) {
                expiration_dateIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_EXPIRATION_DATE);
                maintenance.setExpiration_date(Utils.dateParse(cursor.getString(expiration_dateIndex)));
            }
            if (cursor.getColumnIndex(MAINTENANCE_EXPIRATION_KM) != -1) {
                expiration_kmIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_EXPIRATION_KM);
                maintenance.setExpiration_km(cursor.getInt(expiration_kmIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_ODOMETER) != -1) {
                odometerIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_ODOMETER);
                maintenance.setOdometer(cursor.getInt(odometerIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_VALUE) != -1) {
                valueIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_VALUE);
                maintenance.setValue(cursor.getDouble(valueIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_LOCATION) != -1) {
                locationIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_LOCATION);
                maintenance.setLocation(cursor.getString(locationIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_NOTE) != -1) {
               noteIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_NOTE);
               maintenance.setNote(cursor.getString(noteIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_STATUS) != -1) {
                statusIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_STATUS);
                maintenance.setStatus(cursor.getInt(statusIndex));
            }
        }
        return maintenance;
    }

    private void setContentValue(Maintenance maintenance) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_ID, maintenance.id);
        initialValues.put(MAINTENANCE_VEHICLE_ID, maintenance.vehicle_id);
        initialValues.put(MAINTENANCE_SERVICE_TYPE, maintenance.service_type);
        initialValues.put(MAINTENANCE_DETAIL, maintenance.detail);
        initialValues.put(MAINTENANCE_DATE, Utils.dateFormat(maintenance.date));
        initialValues.put(MAINTENANCE_EXPIRATION_DATE, Utils.dateFormat(maintenance.expiration_date));
        initialValues.put(MAINTENANCE_EXPIRATION_KM, maintenance.expiration_km);
        initialValues.put(MAINTENANCE_ODOMETER, maintenance.odometer);
        initialValues.put(MAINTENANCE_VALUE, maintenance.value);
        initialValues.put(MAINTENANCE_LOCATION, maintenance.location);
        initialValues.put(MAINTENANCE_NOTE, maintenance.note);
        initialValues.put(MAINTENANCE_STATUS, maintenance.status);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

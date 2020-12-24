package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;

import java.util.ArrayList;
import java.util.List;

public class MaintenancePlanDAO extends DbContentProvider implements MaintenancePlanISchema, MaintenancePlanIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public MaintenancePlanDAO(SQLiteDatabase db) {
        super(db);
    }

    public ArrayList<MaintenancePlan> fetchArrayMaintenancePlan(){
        ArrayList<MaintenancePlan> maintenancePlan = new ArrayList<>();
        Cursor cursor = super.query(MAINTENANCE_PLAN_TABLE, MAINTENANCE_PLAN_COLUMNS, null,null, MAINTENANCE_PLAN_DESCRIPTION);
        if(cursor != null && cursor.moveToFirst()){
            do{
                MaintenancePlan m = cursorToEntity(cursor);
                maintenancePlan.add(m);
            }while(cursor.moveToNext());
        }
        return maintenancePlan;
    }

    public MaintenancePlan fetchMaintenancePlanById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MAINTENANCE_PLAN_ID + " = ?";
        MaintenancePlan maintenancePlan = new MaintenancePlan();
        cursor = super.query(MAINTENANCE_PLAN_TABLE, MAINTENANCE_PLAN_COLUMNS, selection, selectionArgs, MAINTENANCE_PLAN_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                maintenancePlan = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return maintenancePlan;
    }

    public MaintenancePlan fetchMaintenancePlanByService(int type, String description) {
        final String[] selectionArgs = { String.valueOf(type), description };
        final String selection = MAINTENANCE_PLAN_SERVICE_TYPE + " = ? AND "+MAINTENANCE_PLAN_DESCRIPTION + " = ?";
        MaintenancePlan maintenancePlan = new MaintenancePlan();
        cursor = super.query(MAINTENANCE_PLAN_TABLE, MAINTENANCE_PLAN_COLUMNS, selection, selectionArgs, MAINTENANCE_PLAN_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                maintenancePlan = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return maintenancePlan;
    }

    public List<MaintenancePlan> fetchAllMaintenancePlan() {
        List<MaintenancePlan> maintenancePlanList = new ArrayList<>();
        cursor = super.query(MAINTENANCE_PLAN_TABLE, MAINTENANCE_PLAN_COLUMNS, null, null, MAINTENANCE_PLAN_SERVICE_TYPE);
        if (cursor.moveToFirst()) {
            do {
                MaintenancePlan maintenancePlan = cursorToEntity(cursor);
                maintenancePlanList.add(maintenancePlan);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return maintenancePlanList;
    }

    public void deleteMaintenancePlan(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MAINTENANCE_PLAN_ID + " = ?";
        super.delete(MAINTENANCE_PLAN_TABLE, selection, selectionArgs);
    }

    public boolean updateMaintenancePlan(MaintenancePlan maintenancePlan) {
        setContentValue(maintenancePlan);
        final String[] selectionArgs = { String.valueOf(maintenancePlan.getId()) };
        final String selection = MAINTENANCE_PLAN_ID + " = ?";
        return (super.update(MAINTENANCE_PLAN_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addMaintenancePlan(MaintenancePlan maintenancePlan) {
        setContentValue(maintenancePlan);
        return (super.insert(MAINTENANCE_PLAN_TABLE, getContentValue()) > 0);
    }

    protected MaintenancePlan cursorToEntity(Cursor cursor) {

        MaintenancePlan maintenancePlan = new MaintenancePlan();

        int idIndex;
        int service_typeIndex;
        int descriptionIndex;
        int measureIndex;
        int expiration_defaultIndex;
        int recommendationIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(MAINTENANCE_PLAN_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_PLAN_ID);
                maintenancePlan.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_PLAN_SERVICE_TYPE) != -1) {
                service_typeIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_PLAN_SERVICE_TYPE);
                maintenancePlan.setService_type(cursor.getInt(service_typeIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_PLAN_DESCRIPTION) != -1) {
                descriptionIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_PLAN_DESCRIPTION);
                maintenancePlan.setDescription(cursor.getString(descriptionIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_PLAN_MEASURE) != -1) {
                measureIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_PLAN_MEASURE);
                maintenancePlan.setMeasure(cursor.getInt(measureIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_PLAN_EXPIRATION_DEFAULT) != -1) {
                expiration_defaultIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_PLAN_EXPIRATION_DEFAULT);
                maintenancePlan.setExpiration_default(cursor.getInt(expiration_defaultIndex));
            }
            if (cursor.getColumnIndex(MAINTENANCE_PLAN_RECOMMENDATION) != -1) {
                recommendationIndex = cursor.getColumnIndexOrThrow(MAINTENANCE_PLAN_RECOMMENDATION);
                maintenancePlan.setRecommendation(cursor.getString(recommendationIndex));
            }
        }
        return maintenancePlan;
    }

    private void setContentValue(MaintenancePlan maintenancePlan) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_PLAN_ID, maintenancePlan.id);
        initialValues.put(MAINTENANCE_PLAN_SERVICE_TYPE, maintenancePlan.service_type);
        initialValues.put(MAINTENANCE_PLAN_DESCRIPTION, maintenancePlan.description);
        initialValues.put(MAINTENANCE_PLAN_MEASURE, maintenancePlan.measure);
        initialValues.put(MAINTENANCE_PLAN_EXPIRATION_DEFAULT, maintenancePlan.expiration_default);
        initialValues.put(MAINTENANCE_PLAN_RECOMMENDATION, maintenancePlan.recommendation);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

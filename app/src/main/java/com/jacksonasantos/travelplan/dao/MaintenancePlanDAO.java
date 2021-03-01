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

    protected MaintenancePlan cursorToEntity(Cursor c) {
        MaintenancePlan mP = new MaintenancePlan();
        if (c != null) {
            if (c.getColumnIndex(MAINTENANCE_PLAN_ID) != -1)                 {mP.setId(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_ID))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_SERVICE_TYPE) != -1)       {mP.setService_type(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_SERVICE_TYPE))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_DESCRIPTION) != -1)        {mP.setDescription(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_DESCRIPTION))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_MEASURE) != -1)            {mP.setMeasure(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_MEASURE))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_EXPIRATION_DEFAULT) != -1) {mP.setExpiration_default(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_EXPIRATION_DEFAULT))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_RECOMMENDATION) != -1)     {mP.setRecommendation(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_RECOMMENDATION))); }
        }
        return mP;
    }

    private void setContentValue(MaintenancePlan mP) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_PLAN_ID, mP.id);
        initialValues.put(MAINTENANCE_PLAN_SERVICE_TYPE, mP.service_type);
        initialValues.put(MAINTENANCE_PLAN_DESCRIPTION, mP.description);
        initialValues.put(MAINTENANCE_PLAN_MEASURE, mP.measure);
        initialValues.put(MAINTENANCE_PLAN_EXPIRATION_DEFAULT, mP.expiration_default);
        initialValues.put(MAINTENANCE_PLAN_RECOMMENDATION, mP.recommendation);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

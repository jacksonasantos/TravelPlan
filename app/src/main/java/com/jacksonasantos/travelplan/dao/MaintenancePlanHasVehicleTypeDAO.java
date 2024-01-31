package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanHasVehicleTypeIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanHasVehicleTypeISchema;

import java.util.ArrayList;
import java.util.List;

public class MaintenancePlanHasVehicleTypeDAO extends DbContentProvider implements MaintenancePlanHasVehicleTypeISchema, MaintenancePlanHasVehicleTypeIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public MaintenancePlanHasVehicleTypeDAO(SQLiteDatabase db) {
        super(db);
    }

    public MaintenancePlanHasVehicleType fetchMaintenancePlanHasVehicleTypeByMaintenancePlanIdVehicleId(Integer maintenance_plan_id, Integer vehicle_type) {
        final String[] selectionArgs = { String.valueOf(maintenance_plan_id), String.valueOf(vehicle_type) };
        final String selection = MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID + " = ? AND " + MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_VEHICLE_TYPE +" = ?";

        MaintenancePlanHasVehicleType maintenancePlanHasVehicleType = new MaintenancePlanHasVehicleType();
        cursor = super.query(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE, MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                maintenancePlanHasVehicleType = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return maintenancePlanHasVehicleType;
    }

    public List<MaintenancePlanHasVehicleType> fetchMaintenancePlanHasVehicleTypeByPlan(Integer maintenance_plan_id) {
        final String[] selectionArgs = { String.valueOf(maintenance_plan_id) };
        final String selection = MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID + " = ?";
        List<MaintenancePlanHasVehicleType> maintenancePlanHasVehicleTypeList = new ArrayList<>();
        cursor = super.query(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE, MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_COLUMNS, selection, selectionArgs, MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID);
        if (cursor.moveToFirst()) {
            do {
                MaintenancePlanHasVehicleType maintenancePlanHasVehicleType = cursorToEntity(cursor);
                maintenancePlanHasVehicleTypeList.add(maintenancePlanHasVehicleType);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return maintenancePlanHasVehicleTypeList;
    }

    public void deleteMaintenancePlanHasVehicleType(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_ID + " = ?";
        super.delete(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE, selection, selectionArgs);
    }

    public boolean updateMaintenancePlanHasVehicleType(MaintenancePlanHasVehicleType maintenancePlanHasVehicleType) {
        setContentValue(maintenancePlanHasVehicleType);
        final String[] selectionArgs = { String.valueOf(maintenancePlanHasVehicleType.getId()) };
        final String selection = MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_ID + " = ?";
        return (super.update(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addMaintenancePlanHasVehicleType(MaintenancePlanHasVehicleType maintenancePlanHasVehicleType) {
        setContentValue(maintenancePlanHasVehicleType);
        return (super.insert(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE, getContentValue()) > 0);
    }

    protected MaintenancePlanHasVehicleType cursorToEntity(Cursor c) {
        MaintenancePlanHasVehicleType mPVT = new MaintenancePlanHasVehicleType();
        if (c != null) {
            if (c.getColumnIndex(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_ID) != -1)                  {mPVT.setId(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_ID))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID) != -1) if (c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID)) == 0) mPVT.setMaintenance_plan_id(null);
                                                                                               else mPVT.setMaintenance_plan_id(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID)));
            if (c.getColumnIndex(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_VEHICLE_TYPE) != -1)        {mPVT.setVehicle_type(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_VEHICLE_TYPE))); }
            if (c.getColumnIndex(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_RECURRING_SERVICE) != -1)   {mPVT.setRecurring_service(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_RECURRING_SERVICE))); }
        }
        return mPVT;
    }

    private void setContentValue(MaintenancePlanHasVehicleType mPVT) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_ID, mPVT.id);
        initialValues.put(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID, mPVT.maintenance_plan_id);
        initialValues.put(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_VEHICLE_TYPE, mPVT.vehicle_type);
        initialValues.put(MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_RECURRING_SERVICE, mPVT.recurring_service);

    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

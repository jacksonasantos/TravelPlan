package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanHasVehicleTypeISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasPlanIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasPlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;

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

    public List<VehicleHasPlanQuery> fetchAllVehicleHasPlanByVehicleWithDefault(Integer vehicle_id) {
        List<VehicleHasPlanQuery> vehicleHasPlanList = new ArrayList<>();
        String sql = "SELECT vp." + VehicleHasPlanISchema.VEHICLE_HAS_PLAN_ID + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_ID+", " +
                            "v." + VehicleISchema.VEHICLE_ID + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_VEHICLE_ID+", " +
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID+", " +
                            "vp." + VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION+", " +
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_EXPIRATION_DEFAULT + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION_DEFAULT+", "+
                            "mpvt." + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_RECURRING_SERVICE + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_RECURRING_SERVICE+", " +
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_MEASURE + " "+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_MEASURE+" "+
                       "FROM "+MaintenancePlanISchema.MAINTENANCE_PLAN_TABLE + " mp "+
                       "JOIN "+MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE +" mpvt " +
                             "ON mpvt."+MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID + " = mp."+MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " " +
                       "JOIN " + VehicleISchema.VEHICLE_TABLE +" v " +
                             "ON mpvt." + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_VEHICLE_TYPE + " = v."+VehicleISchema.VEHICLE_VEHICLE_TYPE + " " +
                       "LEFT JOIN " + VehicleHasPlanISchema.VEHICLE_HAS_PLAN_TABLE + " vp "+
                             "ON (vp." + VehicleHasPlanISchema.VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " = mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " " +
                                 "AND vp." + VehicleHasPlanISchema.VEHICLE_HAS_PLAN_VEHICLE_ID + " = v."+VehicleISchema.VEHICLE_ID + ") " +
                       "WHERE (mpvt." + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_RECURRING_SERVICE +" = 1 OR vp."+VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION + " NOT NULL) " +
                          "AND v." + VehicleISchema.VEHICLE_ID + " = ? "+
                       "ORDER BY mp."+MaintenancePlanISchema.MAINTENANCE_PLAN_SERVICE_TYPE + ", mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_DESCRIPTION;
        cursor = super.rawQuery(sql,
                new String[] { String.valueOf(vehicle_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    VehicleHasPlanQuery vehicleHasPlanQuery = cursorToEntityQuery(cursor);
                    vehicleHasPlanList.add(vehicleHasPlanQuery);
                } while (cursor.moveToNext());
            }
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
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_VEHICLE_ID) != -1)           if (c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_VEHICLE_ID)) == 0) vHP.setVehicle_id(null);
                                                                               else vHP.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_VEHICLE_ID)));
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID) != -1)  if (c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID)) == 0) vHP.setMaintenance_plan_id(null);
                                                                               else vHP.setMaintenance_plan_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID)));
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_EXPIRATION) != -1)           {vHP.setExpiration(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_EXPIRATION))); }
        }
        return vHP;
    }

    protected VehicleHasPlanQuery cursorToEntityQuery(Cursor c) {
        VehicleHasPlanQuery vHPq = new VehicleHasPlanQuery();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_ID) != -1)                   {vHPq.setId(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_ID))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_VEHICLE_ID) != -1)           if (c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_VEHICLE_ID)) == 0) vHPq.setVehicle_id(null);
                                                                               else vHPq.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_VEHICLE_ID)));
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID) != -1)  if (c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID)) == 0) vHPq.setMaintenance_plan_id(null);
                                                                               else vHPq.setMaintenance_plan_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID)));
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_EXPIRATION) != -1)           {vHPq.setExpiration(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_EXPIRATION))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_EXPIRATION_DEFAULT) != -1)   {vHPq.setExpiration_default(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_EXPIRATION_DEFAULT))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_RECURRING_SERVICE) != -1)    {vHPq.setRecurring_service(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_RECURRING_SERVICE))); }
            if (c.getColumnIndex(VEHICLE_HAS_PLAN_MEASURE) != -1)              {vHPq.setMeasure(c.getInt(c.getColumnIndexOrThrow(VEHICLE_HAS_PLAN_MEASURE))); }
        }
        return vHPq;
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

package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanHasVehicleTypeISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.NextMaintenanceItemIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.NextMaintenanceItemISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleHasPlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleISchema;

import java.util.ArrayList;
import java.util.List;

public class NextMaintenanceItemDAO extends DbContentProvider implements NextMaintenanceItemISchema, NextMaintenanceItemIDAO {

    private Cursor cursor;

    public NextMaintenanceItemDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<NextMaintenanceItem> findNextMaintenanceItem(Integer vehicle_id) {
        List<NextMaintenanceItem> nextMaintenanceItemList = new ArrayList<>();

        cursor = super.rawQuery(
                "SELECT mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_SERVICE_TYPE + ", "+
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + ", "+
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_DESCRIPTION + ", "+
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_MEASURE + ", " +
                            "CASE mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_MEASURE + " " +
                                  "WHEN 1 THEN CASE WHEN mi.ult_odometer IS NULL THEN v.odometer_acquisition ELSE mi.ult_odometer END + CASE WHEN vp."+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION + " IS NULL THEN mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_EXPIRATION_DEFAULT + " ELSE vp."+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION + " END " +
                                  "WHEN 2 THEN DATE (JULIANDAY (CASE WHEN mi.ult_date IS NULL THEN v.dt_acquisition ELSE mi.ult_date END) + CASE WHEN vp."+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION + " IS NULL THEN mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_EXPIRATION_DEFAULT + " ELSE vp."+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_EXPIRATION + " END + CAST (JULIANDAY (CASE WHEN mi.ult_date IS NULL THEN v.dt_acquisition ELSE mi.ult_date END) - JULIANDAY ('now') AS INTEGER)) "+
                            "END next_service " +
                      "FROM " + MaintenancePlanISchema.MAINTENANCE_PLAN_TABLE + " mp " +
                      "JOIN " + VehicleISchema.VEHICLE_TABLE + " v " +
                       " ON v." + VehicleISchema.VEHICLE_ID +  " = ? " +
                      "LEFT JOIN (SELECT mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID + ", " +
                                         "m." + MaintenanceISchema.MAINTENANCE_VEHICLE_ID + ", " +
                                         "MAX(m." + MaintenanceISchema.MAINTENANCE_ODOMETER + ") ult_odometer, " +
                                         "MAX(m." + MaintenanceISchema.MAINTENANCE_DATE + ") ult_date " +
                                   "FROM " + MaintenanceItemISchema.MAINTENANCE_ITEM_TABLE + " mi " +
                                   "LEFT JOIN " + MaintenanceISchema.MAINTENANCE_TABLE + " m ON m." + MaintenanceISchema.MAINTENANCE_ID + " = mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_MAINTENANCE_ID + " " +
                                  "WHERE m." + MaintenanceISchema.MAINTENANCE_VEHICLE_ID + " = ? " +
                                  "GROUP BY mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID + ") mi ON mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID + " = mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " " +
                      "LEFT JOIN "+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_TABLE + " vp "+
                             "ON vp."+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_MAINTENANCE_PLAN_ID + " = mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " " +
                            "AND vp."+ VehicleHasPlanISchema.VEHICLE_HAS_PLAN_VEHICLE_ID + " = mi."+  MaintenanceISchema.MAINTENANCE_VEHICLE_ID + " " +
                      "JOIN " + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_TABLE + " mpvt " +
                        "ON mpvt." + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_MAINTENANCE_PLAN_ID +  " = mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " " +
                       "AND mpvt." + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_VEHICLE_TYPE + " = v." + VehicleISchema.VEHICLE_VEHICLE_TYPE + " " +
                     "WHERE mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_MEASURE + " > 0 " +
                       "AND mpvt." + MaintenancePlanHasVehicleTypeISchema.MAINTENANCE_PLAN_HAS_VEHICLE_TYPE_RECURRING_SERVICE + " = 1 " +
                     "ORDER BY next_service",
                new String[] { String.valueOf(vehicle_id), String.valueOf(vehicle_id)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    NextMaintenanceItem nextMaintenanceItem = cursorToEntity(cursor);
                    nextMaintenanceItemList.add(nextMaintenanceItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return nextMaintenanceItemList;
    }

    protected NextMaintenanceItem cursorToEntity(Cursor c) {
        NextMaintenanceItem m = new NextMaintenanceItem();
        if (c != null) {
            if (c.getColumnIndex(NEXT_MAINTENANCE_ITEM_SERVICE_ITEM) != -1)        {m.setService_type(c.getInt(c.getColumnIndexOrThrow(NEXT_MAINTENANCE_ITEM_SERVICE_ITEM))); }
            if (c.getColumnIndex(NEXT_MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID) != -1) {m.setMaintenance_plan_id(c.getInt(c.getColumnIndexOrThrow(NEXT_MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID))); }
            if (c.getColumnIndex(NEXT_MAINTENANCE_ITEM_DESCRIPTION) != -1)         {m.setDescription(c.getString(c.getColumnIndexOrThrow(NEXT_MAINTENANCE_ITEM_DESCRIPTION))); }
            if (c.getColumnIndex(NEXT_MAINTENANCE_ITEM_MEASURE) != -1)             {m.setMeasure(c.getInt(c.getColumnIndexOrThrow(NEXT_MAINTENANCE_ITEM_MEASURE))); }
            if (c.getColumnIndex(NEXT_MAINTENANCE_ITEM_NEXT_SERVICE) != -1)        {m.setNext_service(c.getString(c.getColumnIndexOrThrow(NEXT_MAINTENANCE_ITEM_NEXT_SERVICE))); }
        }
        return m;
    }
}

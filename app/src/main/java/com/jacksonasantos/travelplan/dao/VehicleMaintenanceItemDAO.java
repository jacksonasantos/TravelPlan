package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenancePlanISchema;
import com.jacksonasantos.travelplan.dao.interfaces.VehicleMaintenanceItemISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class VehicleMaintenanceItemDAO extends DbContentProvider implements VehicleMaintenanceItemISchema {

    private Cursor cursor;

    public VehicleMaintenanceItemDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<VehicleMaintenanceItem> findVehicleMaintenanceItems(Integer vehicle_id, Integer service_type) {
        List<VehicleMaintenanceItem> vehicleMaintenanceItemList = new ArrayList<>();

        String sql = "SELECT m." + VehicleMaintenanceItemISchema.VEHICLE_MAINTENANCE_ITEM_VEHICLE_ID + ", " +
                            "mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_SERVICE_TYPE + ", " +
                            "m." + VehicleMaintenanceItemISchema.VEHICLE_MAINTENANCE_ITEM_DATE + ", " +
                            "m." + VehicleMaintenanceItemISchema.VEHICLE_MAINTENANCE_ITEM_ODOMETER + ", " +
                            "mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_DESCRIPTION + ", " +
                            "mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_NOTE + " " +
                       "FROM " + MaintenanceItemISchema.MAINTENANCE_ITEM_TABLE + " mi" +
                          ", " + MaintenanceISchema.MAINTENANCE_TABLE + " m " +
                          ", " + MaintenancePlanISchema.MAINTENANCE_PLAN_TABLE + " mp " +
                      "WHERE m." + MaintenanceISchema.MAINTENANCE_VEHICLE_ID + "= ? ";
        if (service_type == -1) {
            sql += "AND mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_SERVICE_TYPE + " >= ? ";
        } else {
            sql += "AND mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_SERVICE_TYPE + " = ?";
        }
        sql += "AND m." + MaintenanceISchema.MAINTENANCE_ID + " = mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_MAINTENANCE_ID + " " +
               "AND mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID + " = mp." + MaintenancePlanISchema.MAINTENANCE_PLAN_ID + " " +
             "ORDER BY m." + MaintenanceISchema.MAINTENANCE_DATE + " DESC " +
                    ", mi." + MaintenanceItemISchema.MAINTENANCE_ITEM_SERVICE_TYPE + " ASC ";

        cursor = super.rawQuery(sql,
                new String[] { String.valueOf(vehicle_id), String.valueOf(service_type)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    VehicleMaintenanceItem vehicleMaintenanceItem = cursorToEntity(cursor);
                    vehicleMaintenanceItemList.add(vehicleMaintenanceItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return vehicleMaintenanceItemList;
    }

    protected VehicleMaintenanceItem cursorToEntity(Cursor c) {
        VehicleMaintenanceItem vS = new VehicleMaintenanceItem();
        if (c != null) {
            if (c.getColumnIndex(VEHICLE_MAINTENANCE_ITEM_VEHICLE_ID) != -1)   {vS.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(VEHICLE_MAINTENANCE_ITEM_VEHICLE_ID))); }
            if (c.getColumnIndex(VEHICLE_MAINTENANCE_ITEM_SERVICE_TYPE) != -1) {vS.setService_type(c.getInt(c.getColumnIndexOrThrow(VEHICLE_MAINTENANCE_ITEM_SERVICE_TYPE))); }
            if (c.getColumnIndex(VEHICLE_MAINTENANCE_ITEM_DATE) != -1)         {vS.setDate(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(VEHICLE_MAINTENANCE_ITEM_DATE)))); }
            if (c.getColumnIndex(VEHICLE_MAINTENANCE_ITEM_ODOMETER) != -1)     {vS.setOdometer(c.getInt(c.getColumnIndexOrThrow(VEHICLE_MAINTENANCE_ITEM_ODOMETER))); }
            if (c.getColumnIndex(VEHICLE_MAINTENANCE_ITEM_DESCRIPTION) != -1)  {vS.setDescription(c.getString( c.getColumnIndexOrThrow(VEHICLE_MAINTENANCE_ITEM_DESCRIPTION))); }
            if (c.getColumnIndex(VEHICLE_MAINTENANCE_ITEM_NOTE) != -1)         {vS.setNote(c.getString( c.getColumnIndexOrThrow(VEHICLE_MAINTENANCE_ITEM_NOTE))); }
        }
        return vS;
    }
}
package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceItemDAO extends DbContentProvider implements MaintenanceItemISchema, MaintenanceItemIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public MaintenanceItemDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<MaintenanceItem> fetchMaintenanceItemByMaintenance(Integer maintenance_id) {
        final String[] selectionArgs = { String.valueOf(maintenance_id) };
        final String selection = MAINTENANCE_ITEM_MAINTENANCE_ID + " = ?";
        List<MaintenanceItem> maintenanceItemList = new ArrayList<>();
        cursor = super.query(MAINTENANCE_ITEM_TABLE, MAINTENANCE_ITEM_COLUMNS, selection, selectionArgs, MAINTENANCE_ITEM_ID);

        if (cursor.moveToFirst()) {
            do {
                MaintenanceItem maintenanceItem = cursorToEntity(cursor);
                maintenanceItemList.add(maintenanceItem);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return maintenanceItemList;
    }

    public void deleteMaintenanceItem(Integer maintenance_id, Integer id) {
        final String[] selectionArgs = { String.valueOf(maintenance_id), String.valueOf(id) };
        final String selection = MAINTENANCE_ITEM_MAINTENANCE_ID + " = ? AND " + MAINTENANCE_ITEM_ID + " = ?";
        super.delete(MAINTENANCE_ITEM_TABLE, selection, selectionArgs);
    }

    public boolean addMaintenanceItem(MaintenanceItem maintenanceItem) {
        setContentValue(maintenanceItem);
        return (super.insert(MAINTENANCE_ITEM_TABLE, getContentValue()) > 0);
    }

    protected MaintenanceItem cursorToEntity(Cursor c) {
        MaintenanceItem m = new MaintenanceItem();
        if (c != null) {
            if (c.getColumnIndex(MAINTENANCE_ITEM_ID) != -1)                  {m.setId(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_ID))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID) != -1) {m.setMaintenance_plan_id(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_MAINTENANCE_ID) != -1)      {m.setMaintenance_id(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_MAINTENANCE_ID))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_SERVICE_TYPE) != -1)        {m.setService_type(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_SERVICE_TYPE))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_MEASURE_TYPE) != -1)        {m.setMeasure_type(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_MEASURE_TYPE))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_EXPIRATION_VALUE) != -1)    {m.setExpiration_value(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_EXPIRATION_VALUE))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_VALUE) != -1)               {m.setValue(c.getDouble(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_VALUE))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_NOTE) != -1)                {m.setNote(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_NOTE))); }
        }
        return m;
    }

    private void setContentValue(MaintenanceItem m) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_ITEM_ID, m.id);
        initialValues.put(MAINTENANCE_ITEM_MAINTENANCE_PLAN_ID, m.maintenance_plan_id);
        initialValues.put(MAINTENANCE_ITEM_MAINTENANCE_ID, m.maintenance_id);
        initialValues.put(MAINTENANCE_ITEM_SERVICE_TYPE, m.service_type);
        initialValues.put(MAINTENANCE_ITEM_MEASURE_TYPE, m.measure_type);
        initialValues.put(MAINTENANCE_ITEM_EXPIRATION_VALUE, m.expiration_value);
        initialValues.put(MAINTENANCE_ITEM_VALUE, m.value);
        initialValues.put(MAINTENANCE_ITEM_NOTE, m.note);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

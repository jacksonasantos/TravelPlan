package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.MaintenanceItemISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceItemDAO extends DbContentProvider implements MaintenanceItemISchema, MaintenanceItemIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public MaintenanceItemDAO(SQLiteDatabase db) {
        super(db);
    }

    public MaintenanceItem fetchMaintenanceItemById(Integer maintenance_id, Integer id) {
        final String[] selectionArgs = { String.valueOf(maintenance_id), String.valueOf(id) };
        final String selection = MAINTENANCE_ITEM_MAINTENANCE_ID + " = ? AND " + MAINTENANCE_ITEM_ID + " = ?";
        MaintenanceItem maintenanceItem = new MaintenanceItem();
        cursor = super.query(MAINTENANCE_ITEM_TABLE, MAINTENANCE_ITEM_COLUMNS, selection, selectionArgs, MAINTENANCE_ITEM_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                maintenanceItem = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return maintenanceItem;
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

    public List<MaintenanceItem> fetchAllMaintenanceItem() {
        List<MaintenanceItem> maintenanceItemList = new ArrayList<>();

        cursor = super.query(MAINTENANCE_ITEM_TABLE, MAINTENANCE_ITEM_COLUMNS, null, null, null);

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

    public boolean updateMaintenanceItem(MaintenanceItem maintenanceItem) {
        setContentValue(maintenanceItem);
        final String[] selectionArgs = { String.valueOf(maintenanceItem.getMaintenance_id()), String.valueOf(maintenanceItem.getId()) };
        final String selection = MAINTENANCE_ITEM_MAINTENANCE_ID + " = ? AND " + MAINTENANCE_ITEM_ID + " = ?";
        return (super.update(MAINTENANCE_ITEM_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addMaintenanceItem(MaintenanceItem maintenanceItem) {
        setContentValue(maintenanceItem);
        return (super.insert(MAINTENANCE_ITEM_TABLE, getContentValue()) > 0);
    }

    protected MaintenanceItem cursorToEntity(Cursor c) {
        MaintenanceItem m = new MaintenanceItem();
        if (c != null) {
            if (c.getColumnIndex(MAINTENANCE_ITEM_ID) != -1)              {m.setId(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_ID))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_MAINTENANCE_ID) != -1)  {m.setMaintenance_id(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_MAINTENANCE_ID))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_SERVICE_TYPE) != -1)    {m.setService_type(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_SERVICE_TYPE))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_EXPIRATION_DATE) != -1) {m.setExpiration_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_EXPIRATION_DATE)))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_EXPIRATION_KM) != -1)   {m.setExpiration_km(c.getInt(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_EXPIRATION_KM))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_VALUE) != -1)           {m.setValue(c.getDouble(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_VALUE))); }
            if (c.getColumnIndex(MAINTENANCE_ITEM_NOTE) != -1)            {m.setNote(c.getString(c.getColumnIndexOrThrow(MAINTENANCE_ITEM_NOTE))); }
        }
        return m;
    }

    private void setContentValue(MaintenanceItem m) {
        initialValues = new ContentValues();
        initialValues.put(MAINTENANCE_ITEM_ID, m.id);
        initialValues.put(MAINTENANCE_ITEM_MAINTENANCE_ID, m.maintenance_id);
        initialValues.put(MAINTENANCE_ITEM_SERVICE_TYPE, m.service_type);
        initialValues.put(MAINTENANCE_ITEM_EXPIRATION_DATE, Utils.dateFormat(m.expiration_date));
        initialValues.put(MAINTENANCE_ITEM_EXPIRATION_KM, m.expiration_km);
        initialValues.put(MAINTENANCE_ITEM_VALUE, m.value);
        initialValues.put(MAINTENANCE_ITEM_NOTE, m.note);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

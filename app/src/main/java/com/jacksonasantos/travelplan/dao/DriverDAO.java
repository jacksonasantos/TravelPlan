package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.DriverIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.DriverISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverDAO extends DbContentProvider implements DriverISchema, DriverIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public DriverDAO(SQLiteDatabase db) {
        super(db);
    }

    public Driver fetchDriverById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = DRIVER_ID + " = ?";
        Driver driver = new Driver();
        cursor = super.query(DRIVER_TABLE, DRIVER_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                driver = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return driver;
    }

    public List<Driver> fetchAllDriver() {
        List<Driver> driverList = new ArrayList<>();

        cursor = super.query(DRIVER_TABLE, DRIVER_COLUMNS, null,null, DRIVER_NAME);

        if (cursor.moveToFirst()) {
            do {
                Driver driver = cursorToEntity(cursor);
                driverList.add(driver);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return driverList;
    }

    public ArrayList<Driver> fetchArrayDriver(){
        ArrayList<Driver> driverList = new ArrayList<>();
        Cursor cursor = super.query(DRIVER_TABLE, DRIVER_COLUMNS, null,null, DRIVER_NAME);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Driver driver = cursorToEntity(cursor);
                driverList.add(driver);
            }while(cursor.moveToNext());
        }
        return driverList;
    }

    public void deleteDriver(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = DRIVER_ID + " = ?";
        super.delete(DRIVER_TABLE, selection, selectionArgs);
    }

    public boolean updateDriver(Driver driver) {
        setContentValue(driver);
        final String[] selectionArgs = { String.valueOf(driver.getId()) };
        final String selection = DRIVER_ID + " = ?";
        try {
            return (super.update(DRIVER_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    public boolean addDriver(Driver driver) {
        setContentValue(driver);
        try {
            return (super.insert(DRIVER_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    protected Driver cursorToEntity(Cursor c) {
        Driver b = new Driver();
        if (c != null) {
            if (c.getColumnIndex(DRIVER_ID) != -1)                      {b.id = c.getInt(c.getColumnIndexOrThrow(DRIVER_ID)); }
            if (c.getColumnIndex(DRIVER_NAME) != -1)                    {b.name = c.getString(c.getColumnIndexOrThrow(DRIVER_NAME)); }
            if (c.getColumnIndex(DRIVER_SHORT_NAME) != -1)              {b.short_name = c.getString(c.getColumnIndexOrThrow(DRIVER_SHORT_NAME)); }
            if (c.getColumnIndex(DRIVER_GENDER) != -1)                  {b.gender = c.getInt(c.getColumnIndexOrThrow(DRIVER_GENDER)); }
            if (c.getColumnIndex(DRIVER_DRIVING_RECORD) != -1)          {b.driving_record = c.getString(c.getColumnIndexOrThrow(DRIVER_DRIVING_RECORD)); }
            if (c.getColumnIndex(DRIVER_LICENSE_EXPIRATION_DATE) != -1) {b.license_expiration_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(DRIVER_LICENSE_EXPIRATION_DATE))); }
            if (c.getColumnIndex(DRIVER_FIRST_LICENSE_DATE) != -1)      {b.first_license_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(DRIVER_FIRST_LICENSE_DATE))); }
            if (c.getColumnIndex(DRIVER_LICENCE_ISSUE_DATE) != -1)      {b.licence_issue_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(DRIVER_LICENCE_ISSUE_DATE))); }
            if (c.getColumnIndex(DRIVER_CATEGORY) != -1)                {b.category = c.getString(c.getColumnIndexOrThrow(DRIVER_CATEGORY));
            }
        }
        return b;
    }

    private void setContentValue(Driver b) {
        initialValues = new ContentValues();
        initialValues.put(DRIVER_ID, b.id);
        initialValues.put(DRIVER_NAME, b.name);
        initialValues.put(DRIVER_SHORT_NAME, b.short_name);
        initialValues.put(DRIVER_GENDER, b.gender);
        initialValues.put(DRIVER_DRIVING_RECORD, b.driving_record);
        initialValues.put(DRIVER_LICENSE_EXPIRATION_DATE, Utils.dateFormat(b.license_expiration_date));
        initialValues.put(DRIVER_FIRST_LICENSE_DATE, Utils.dateFormat(b.first_license_date));
        initialValues.put(DRIVER_LICENCE_ISSUE_DATE, Utils.dateFormat(b.licence_issue_date));
        initialValues.put(DRIVER_CATEGORY, b.category);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

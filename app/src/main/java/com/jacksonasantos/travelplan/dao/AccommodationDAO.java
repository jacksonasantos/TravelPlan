package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.AccommodationIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AccommodationISchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccommodationDAO extends DbContentProvider implements AccommodationISchema, AccommodationIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public AccommodationDAO(SQLiteDatabase db) {
        super(db);
    }

    public Accommodation fetchAccommodationById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACCOMMODATION_ID + " = ?";
        Accommodation accommodation = new Accommodation();
        cursor = super.query(ACCOMMODATION_TABLE, ACCOMMODATION_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                accommodation = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return accommodation;
    }

    public List<Accommodation> fetchAllAccommodation() {
        List<Accommodation> accommodationList = new ArrayList<>();
        cursor = super.query(ACCOMMODATION_TABLE, ACCOMMODATION_COLUMNS, null,null, ACCOMMODATION_NAME);
        if (cursor.moveToFirst()) {
            do {
                Accommodation accommodation = cursorToEntity(cursor);
                accommodationList.add(accommodation);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return accommodationList;
    }

    public ArrayList<Accommodation> fetchArrayAccommodation(){
        ArrayList<Accommodation> accommodationList = new ArrayList<>();
        Cursor cursor = super.query(ACCOMMODATION_TABLE, ACCOMMODATION_COLUMNS, null,null, ACCOMMODATION_NAME);
        if(cursor != null && cursor.moveToFirst()){
            do {
                Accommodation accommodation = cursorToEntity(cursor);
                accommodationList.add(accommodation);
            } while(cursor.moveToNext());
        }
        return accommodationList;
    }

    public void deleteAccommodation(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACCOMMODATION_ID + " = ?";
        super.delete(ACCOMMODATION_TABLE, selection, selectionArgs);
    }

    public boolean updateAccommodation(Accommodation accommodation) {
        setContentValue(accommodation);
        final String[] selectionArgs = { String.valueOf(accommodation.getId()) };
        final String selection = ACCOMMODATION_ID + " = ?";
        try {
            return (super.update(ACCOMMODATION_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    public boolean addAccommodation(Accommodation accommodation) {
        setContentValue(accommodation);
        try {
            return (super.insert(ACCOMMODATION_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    protected Accommodation cursorToEntity(Cursor c) {
        Accommodation a = new Accommodation();
        if (c != null) {
            if (c.getColumnIndex(ACCOMMODATION_ID) != -1)                   {a.setId(c.getInt(c.getColumnIndexOrThrow(ACCOMMODATION_ID))); }
            if (c.getColumnIndex(ACCOMMODATION_NAME) != -1)                 {a.setName(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_NAME))); }
            if (c.getColumnIndex(ACCOMMODATION_ADDRESS) != -1)              {a.setAddress(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_ADDRESS))); }
            if (c.getColumnIndex(ACCOMMODATION_CITY) != -1)                 {a.setCity(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_CITY))); }
            if (c.getColumnIndex(ACCOMMODATION_STATE) != -1)                {a.setState(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_STATE))); }
            if (c.getColumnIndex(ACCOMMODATION_COUNTRY) != -1)              {a.setCountry(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_COUNTRY))); }
            if (c.getColumnIndex(ACCOMMODATION_CONTACT_NAME) != -1)         {a.setContact_name( c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_CONTACT_NAME))); }
            if (c.getColumnIndex(ACCOMMODATION_PHONE) != -1)                {a.setPhone(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_PHONE))); }
            if (c.getColumnIndex(ACCOMMODATION_EMAIL) != -1)                {a.setEmail(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_EMAIL))); }
            if (c.getColumnIndex(ACCOMMODATION_SITE) != -1)                 {a.setSite(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_SITE))); }
            if (c.getColumnIndex(ACCOMMODATION_LATLNG_ACCOMMODATION) != -1) {a.setLatlng_accommodation(c.getString(c.getColumnIndexOrThrow(ACCOMMODATION_LATLNG_ACCOMMODATION))); }
            if (c.getColumnIndex(ACCOMMODATION_ACCOMMODATION_TYPE) != -1)   {a.setAccommodation_type(c.getInt(c.getColumnIndexOrThrow(ACCOMMODATION_ACCOMMODATION_TYPE)));   }
        }
        return a;
    }

    private void setContentValue(Accommodation a) {
        initialValues = new ContentValues();
        initialValues.put(ACCOMMODATION_ID, a.id);
        initialValues.put(ACCOMMODATION_NAME, a.name);
        initialValues.put(ACCOMMODATION_ADDRESS, a.address);
        initialValues.put(ACCOMMODATION_CITY, a.city);
        initialValues.put(ACCOMMODATION_STATE, a.state);
        initialValues.put(ACCOMMODATION_COUNTRY, a.country);
        initialValues.put(ACCOMMODATION_CONTACT_NAME, a.contact_name);
        initialValues.put(ACCOMMODATION_PHONE, a.phone);
        initialValues.put(ACCOMMODATION_EMAIL, a.email);
        initialValues.put(ACCOMMODATION_SITE, a.site);
        initialValues.put(ACCOMMODATION_LATLNG_ACCOMMODATION, a.latlng_accommodation);
        initialValues.put(ACCOMMODATION_ACCOMMODATION_TYPE, a.accommodation_type);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

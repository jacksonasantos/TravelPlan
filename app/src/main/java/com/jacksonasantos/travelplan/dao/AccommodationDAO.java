package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
            do{
                Accommodation accommodation = cursorToEntity(cursor);
                accommodationList.add(accommodation);
            }while(cursor.moveToNext());
        }
        return accommodationList;
    }

    public void deleteAccommodation(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACCOMMODATION_ID + " = ?";
        super.delete(ACCOMMODATION_TABLE, selection, selectionArgs);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean addAccommodation(Accommodation accommodation) {
        setContentValue(accommodation);
        try {
            return (super.insert(ACCOMMODATION_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    protected Accommodation cursorToEntity(Cursor cursor) {

        Accommodation accommodation = new Accommodation();

        int idIndex;
        int nameIndex;
        int addressIndex;
        int cityIndex;
        int stateIndex;
        int countryIndex;
        int contact_nameIndex;
        int phoneIndex;
        int emailIndex;
        int siteIndex;
        int latlng_accommodationIndex;
        int accommodation_typeIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(ACCOMMODATION_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_ID);
                accommodation.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_NAME) != -1) {
                nameIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_NAME);
                accommodation.setName(cursor.getString(nameIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_ADDRESS) != -1) {
                addressIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_ADDRESS);
                accommodation.setAddress(cursor.getString(addressIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_CITY) != -1) {
                cityIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_CITY);
                accommodation.setCity(cursor.getString(cityIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_STATE) != -1) {
                stateIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_STATE);
                accommodation.setState(cursor.getString(stateIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_COUNTRY) != -1) {
                countryIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_COUNTRY);
                accommodation.setCountry(cursor.getString(countryIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_CONTACT_NAME) != -1) {
                contact_nameIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_CONTACT_NAME);
                accommodation.contact_name = cursor.getString(contact_nameIndex);
            }
            if (cursor.getColumnIndex(ACCOMMODATION_PHONE) != -1) {
                phoneIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_PHONE);
                accommodation.phone = cursor.getString(phoneIndex);
            }
            if (cursor.getColumnIndex(ACCOMMODATION_EMAIL) != -1) {
                emailIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_EMAIL);
                accommodation.email = cursor.getString(emailIndex);
            }
            if (cursor.getColumnIndex(ACCOMMODATION_SITE) != -1) {
                siteIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_SITE);
                accommodation.setSite(cursor.getString(siteIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_LATLNG_ACCOMMODATION) != -1) {
                latlng_accommodationIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_LATLNG_ACCOMMODATION);
                accommodation.setLatlng_accommodation(cursor.getString(latlng_accommodationIndex));
            }
            if (cursor.getColumnIndex(ACCOMMODATION_ACCOMMODATION_TYPE) != -1) {
                accommodation_typeIndex = cursor.getColumnIndexOrThrow(ACCOMMODATION_ACCOMMODATION_TYPE);
                accommodation.setAccommodation_type(cursor.getInt(accommodation_typeIndex));
            }
        }
        return accommodation;
    }

    private void setContentValue(Accommodation accommodation) {
        initialValues = new ContentValues();
        initialValues.put(ACCOMMODATION_ID, accommodation.id);
        initialValues.put(ACCOMMODATION_NAME, accommodation.name);
        initialValues.put(ACCOMMODATION_ADDRESS, accommodation.address);
        initialValues.put(ACCOMMODATION_CITY, accommodation.city);
        initialValues.put(ACCOMMODATION_STATE, accommodation.state);
        initialValues.put(ACCOMMODATION_COUNTRY, accommodation.country);
        initialValues.put(ACCOMMODATION_CONTACT_NAME, accommodation.contact_name);
        initialValues.put(ACCOMMODATION_PHONE, accommodation.phone);
        initialValues.put(ACCOMMODATION_EMAIL, accommodation.email);
        initialValues.put(ACCOMMODATION_SITE, accommodation.site);
        initialValues.put(ACCOMMODATION_LATLNG_ACCOMMODATION, accommodation.latlng_accommodation);
        initialValues.put(ACCOMMODATION_ACCOMMODATION_TYPE, accommodation.accommodation_type);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

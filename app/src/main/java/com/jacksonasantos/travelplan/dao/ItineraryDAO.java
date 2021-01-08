package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.interfaces.ItineraryIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryISchema;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDAO extends DbContentProvider implements ItineraryISchema, ItineraryIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public ItineraryDAO(SQLiteDatabase db) {
        super(db);
    }

    public Itinerary fetchItineraryById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ITINERARY_ID + " = ?";
        Itinerary itinerary = new Itinerary();
        cursor = super.query(ITINERARY_TABLE, ITINERARY_COLUMNS, selection, selectionArgs, ITINERARY_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                itinerary = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return itinerary;
    }

    public List<Itinerary> fetchItineraryByTravelId(Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = ITINERARY_TRAVEL_ID + " = ?";
        List<Itinerary> itineraryList = new ArrayList<>();
        cursor = super.query(ITINERARY_TABLE, ITINERARY_COLUMNS, selection, selectionArgs, ITINERARY_ID);
        if (cursor.moveToFirst()) {
            do {
                Itinerary itinerary = cursorToEntity(cursor);
                itineraryList.add(itinerary);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return itineraryList;
    }

    public List<Itinerary> fetchAllItinerary() {
        List<Itinerary> itineraryList = new ArrayList<>();
        cursor = super.query(ITINERARY_TABLE, ITINERARY_COLUMNS, null, null, ITINERARY_TRAVEL_ID);
        if (cursor.moveToFirst()) {
            do {
                Itinerary itinerary = cursorToEntity(cursor);
                itineraryList.add(itinerary);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return itineraryList;
    }

    public void deleteItinerary(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ITINERARY_ID + " = ?";
        super.delete(ITINERARY_TABLE, selection, selectionArgs);
    }

    public boolean updateItinerary(Itinerary itinerary) {
        setContentValue(itinerary);
        final String[] selectionArgs = { String.valueOf(itinerary.getId()) };
        final String selection = ITINERARY_ID + " = ?";
        return (super.update(ITINERARY_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addItinerary(Itinerary itinerary) {
        setContentValue(itinerary);
        return (super.insert(ITINERARY_TABLE, getContentValue()) > 0);
    }

    protected Itinerary cursorToEntity(Cursor cursor) {

        Itinerary itinerary = new Itinerary();

        int idIndex;
        int travel_idIndex;
        int nameIndex;
        int addressIndex;
        int category_typeIndex;
        int descriptionIndex;
        int latitudeIndex;
        int longitudeIndex;
        int zoom_levelIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(ITINERARY_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(ITINERARY_ID);
                itinerary.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_TRAVEL_ID) != -1) {
                travel_idIndex = cursor.getColumnIndexOrThrow(ITINERARY_TRAVEL_ID);
                itinerary.setTravel_id(cursor.getInt(travel_idIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_NAME) != -1) {
                nameIndex = cursor.getColumnIndexOrThrow(ITINERARY_NAME);
                itinerary.setName(cursor.getString(nameIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_ADDRESS) != -1) {
                addressIndex = cursor.getColumnIndexOrThrow(ITINERARY_ADDRESS);
                itinerary.setAddress(cursor.getString(addressIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_CATEGORY_TYPE) != -1) {
                category_typeIndex = cursor.getColumnIndexOrThrow(ITINERARY_CATEGORY_TYPE);
                itinerary.setCategory_type(cursor.getInt(category_typeIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_DESCRIPTION) != -1) {
                descriptionIndex = cursor.getColumnIndexOrThrow(ITINERARY_DESCRIPTION);
                itinerary.setDescription(cursor.getString(descriptionIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_LATITUDE) != -1) {
                latitudeIndex = cursor.getColumnIndexOrThrow(ITINERARY_LATITUDE);
                itinerary.setLatitude(cursor.getString(latitudeIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_LONGITUDE) != -1) {
                longitudeIndex = cursor.getColumnIndexOrThrow(ITINERARY_LONGITUDE);
                itinerary.setLongitude(cursor.getString(longitudeIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_ZOOM_LEVEL) != -1) {
                zoom_levelIndex = cursor.getColumnIndexOrThrow(ITINERARY_ZOOM_LEVEL);
                itinerary.setZoom_level(cursor.getString(zoom_levelIndex));
            }
        }
        return itinerary;
    }

    private void setContentValue(Itinerary itinerary) {
        initialValues = new ContentValues();
        initialValues.put(ITINERARY_ID, itinerary.id);
        initialValues.put(ITINERARY_TRAVEL_ID, itinerary.travel_id);
        initialValues.put(ITINERARY_NAME, itinerary.name);
        initialValues.put(ITINERARY_ADDRESS, itinerary.address);
        initialValues.put(ITINERARY_CATEGORY_TYPE, itinerary.category_type);
        initialValues.put(ITINERARY_DESCRIPTION, itinerary.description);
        initialValues.put(ITINERARY_LATITUDE, itinerary.latitude);
        initialValues.put(ITINERARY_LONGITUDE, itinerary.longitude);
        initialValues.put(ITINERARY_ZOOM_LEVEL, itinerary.zoom_level);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

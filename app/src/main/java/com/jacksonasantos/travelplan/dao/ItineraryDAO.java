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
    
    public List<Itinerary> fetchAllItineraryByTravel(Integer travel_id) {
        List<Itinerary> itineraryList = new ArrayList<>();
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = ITINERARY_TRAVEL_ID + " = ?";

        cursor = super.query(ITINERARY_TABLE, ITINERARY_COLUMNS, selection, selectionArgs, ITINERARY_SEQUENCE);
        if (cursor.moveToFirst()) {
            do {
                Itinerary itinerary = cursorToEntity(cursor);
                itineraryList.add(itinerary);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return itineraryList;
    }
    
    public List<Itinerary> fetchAllItinerary( ) {
        List<Itinerary> itineraryList = new ArrayList<>();

        cursor = super.query(ITINERARY_TABLE, ITINERARY_COLUMNS, null, null, null);

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
        int sequenceIndex;
        int orig_locationIndex;
        int dest_locationIndex;
        int latlng_trip_origIndex;
        int latlng_trip_destIndex;
        int dailyIndex;
        int distanceIndex;
        int timeIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(ITINERARY_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(ITINERARY_ID);
                itinerary.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_TRAVEL_ID) != -1) {
                travel_idIndex = cursor.getColumnIndexOrThrow(ITINERARY_TRAVEL_ID);
                itinerary.setTravel_id(cursor.getInt(travel_idIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_SEQUENCE) != -1) {
                sequenceIndex = cursor.getColumnIndexOrThrow(ITINERARY_SEQUENCE);
                itinerary.setSequence(cursor.getInt(sequenceIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_ORIG_LOCATION) != -1) {
                orig_locationIndex = cursor.getColumnIndexOrThrow(ITINERARY_ORIG_LOCATION);
                itinerary.setOrig_location(cursor.getString(orig_locationIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_DEST_LOCATION) != -1) {
                dest_locationIndex = cursor.getColumnIndexOrThrow(ITINERARY_DEST_LOCATION);
                itinerary.setDest_location(cursor.getString(dest_locationIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_LATLNG_TRIP_ORIG) != -1) {
                latlng_trip_origIndex = cursor.getColumnIndexOrThrow(ITINERARY_LATLNG_TRIP_ORIG);
                itinerary.setLatlng_trip_orig(cursor.getString(latlng_trip_origIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_LATLNG_TRIP_DEST) != -1) {
                latlng_trip_destIndex = cursor.getColumnIndexOrThrow(ITINERARY_LATLNG_TRIP_DEST);
                itinerary.setLatlng_trip_dest(cursor.getString(latlng_trip_destIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_DISTANCE) != -1) {
                distanceIndex = cursor.getColumnIndexOrThrow(ITINERARY_DISTANCE);
                itinerary.setDistance(cursor.getInt(distanceIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_DAILY) != -1) {
                dailyIndex = cursor.getColumnIndexOrThrow(ITINERARY_DAILY);
                itinerary.setDaily(cursor.getInt(dailyIndex));
            }
            if (cursor.getColumnIndex(ITINERARY_TIME) != -1) {
                timeIndex = cursor.getColumnIndexOrThrow(ITINERARY_TIME);
                itinerary.setTime(cursor.getString(timeIndex));
            }
        }
        return itinerary;
    }

    private void setContentValue(Itinerary itinerary) {

        initialValues = new ContentValues();
        initialValues.put(ITINERARY_ID, itinerary.id);
        initialValues.put(ITINERARY_TRAVEL_ID, itinerary.travel_id);
        initialValues.put(ITINERARY_SEQUENCE, itinerary.sequence);
        initialValues.put(ITINERARY_ORIG_LOCATION, itinerary.orig_location);
        initialValues.put(ITINERARY_DEST_LOCATION, itinerary.dest_location);
        initialValues.put(ITINERARY_LATLNG_TRIP_ORIG, itinerary.latlng_trip_orig);
        initialValues.put(ITINERARY_LATLNG_TRIP_DEST, itinerary.latlng_trip_dest);
        initialValues.put(ITINERARY_DAILY, itinerary.daily);
        initialValues.put(ITINERARY_DISTANCE, itinerary.distance);
        initialValues.put(ITINERARY_TIME, itinerary.time);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

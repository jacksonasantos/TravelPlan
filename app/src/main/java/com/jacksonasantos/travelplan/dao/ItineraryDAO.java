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

    protected Itinerary cursorToEntity(Cursor c) {
        Itinerary i = new Itinerary();
        if (c != null) {
            if (c.getColumnIndex(ITINERARY_ID) != -1)               {i.setId(c.getInt(c.getColumnIndexOrThrow(ITINERARY_ID))); }
            if (c.getColumnIndex(ITINERARY_TRAVEL_ID) != -1)        {i.setTravel_id(c.getInt(c.getColumnIndexOrThrow(ITINERARY_TRAVEL_ID))); }
            if (c.getColumnIndex(ITINERARY_SEQUENCE) != -1)         {i.setSequence(c.getInt(c.getColumnIndexOrThrow(ITINERARY_SEQUENCE))); }
            if (c.getColumnIndex(ITINERARY_ORIG_LOCATION) != -1)    {i.setOrig_location(c.getString(c.getColumnIndexOrThrow(ITINERARY_ORIG_LOCATION))); }
            if (c.getColumnIndex(ITINERARY_DEST_LOCATION) != -1)    {i.setDest_location(c.getString(c.getColumnIndexOrThrow(ITINERARY_DEST_LOCATION))); }
            if (c.getColumnIndex(ITINERARY_LATLNG_TRIP_ORIG) != -1) {i.setLatlng_trip_orig(c.getString(c.getColumnIndexOrThrow(ITINERARY_LATLNG_TRIP_ORIG))); }
            if (c.getColumnIndex(ITINERARY_LATLNG_TRIP_DEST) != -1) {i.setLatlng_trip_dest(c.getString(c.getColumnIndexOrThrow(ITINERARY_LATLNG_TRIP_DEST))); }
            if (c.getColumnIndex(ITINERARY_DISTANCE) != -1)         {i.setDistance(c.getInt(c.getColumnIndexOrThrow(ITINERARY_DISTANCE))); }
            if (c.getColumnIndex(ITINERARY_DAILY) != -1)            {i.setDaily(c.getInt(c.getColumnIndexOrThrow(ITINERARY_DAILY))); }
            if (c.getColumnIndex(ITINERARY_TIME) != -1)             {i.setTime(c.getString(c.getColumnIndexOrThrow(ITINERARY_TIME))); }
        }
        return i;
    }

    private void setContentValue(Itinerary i) {
        initialValues = new ContentValues();
        initialValues.put(ITINERARY_ID, i.id);
        initialValues.put(ITINERARY_TRAVEL_ID, i.travel_id);
        initialValues.put(ITINERARY_SEQUENCE, i.sequence);
        initialValues.put(ITINERARY_ORIG_LOCATION, i.orig_location);
        initialValues.put(ITINERARY_DEST_LOCATION, i.dest_location);
        initialValues.put(ITINERARY_LATLNG_TRIP_ORIG, i.latlng_trip_orig);
        initialValues.put(ITINERARY_LATLNG_TRIP_DEST, i.latlng_trip_dest);
        initialValues.put(ITINERARY_DAILY, i.daily);
        initialValues.put(ITINERARY_DISTANCE, i.distance);
        initialValues.put(ITINERARY_TIME, i.time);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

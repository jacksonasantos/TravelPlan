package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.MarkerIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.MarkerISchema;

import java.util.ArrayList;
import java.util.List;

public class MarkerDAO extends DbContentProvider implements MarkerISchema, MarkerIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public MarkerDAO(SQLiteDatabase db) {
        super(db);
    }

    public Marker fetchMarkerByPoint(Integer travel_id, LatLng point) {
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(point.latitude), String.valueOf(point.longitude)};
        final String selection = MARKER_TRAVEL_ID + " = ? AND " + MARKER_LATITUDE + " = ? AND " + MARKER_LONGITUDE + " = ?";
        Marker marker = new Marker();
        cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                marker = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return marker;
    }

    public Marker fetchMarkerById(Integer marker_id) {
        final String[] selectionArgs = { String.valueOf(marker_id)};
        final String selection = MARKER_ID + " = ? ";
        Marker marker = new Marker();
        cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                marker = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return marker;
    }

    public List<Marker> fetchMarkerByTravelItineraryId(Integer travel_id, Integer itinerary_id) {
        List<Marker> markerList = new ArrayList<>();
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(itinerary_id) };
        final String selection = MARKER_TRAVEL_ID + " = ? AND " + MARKER_ITINERARY_ID + " = ? ";

        cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_SEQUENCE);

        if (cursor.moveToFirst()) {
            do {
                Marker marker = cursorToEntity(cursor);
                markerList.add(marker);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return markerList;
    }

    public Marker fetchLastMarkerByTravelItinerary(Integer travel_id, Integer itinerary_id) {
        Marker marker = new Marker();
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(itinerary_id) };
        final String selection = MARKER_TRAVEL_ID + " = ? AND "+ MARKER_ITINERARY_ID + " = ? ";

        cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_SEQUENCE + " DESC ");
        if (cursor.moveToFirst()) {
            marker = cursorToEntity(cursor);
            cursor.close();
        }
        return marker;
    }

    public List<Marker> fetchMarkerByTravelId(Integer travel_id) {
        List<Marker> markerList = new ArrayList<>();
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = MARKER_TRAVEL_ID + " = ? ";

        cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_ITINERARY_ID+","+MARKER_SEQUENCE);

        if (cursor.moveToFirst()) {
            do {
                Marker marker = cursorToEntity(cursor);
                markerList.add(marker);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return markerList;
    }

    public void deleteMarker(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MARKER_ID + " = ?";
        super.delete(MARKER_TABLE, selection, selectionArgs);
    }

    public boolean deleteMarker(Integer travel_id, String latitude, String longitude) {
        final String[] selectionArgs = { String.valueOf(travel_id), latitude, longitude };
        final String selection = MARKER_TRAVEL_ID + " = ? AND " + MARKER_LATITUDE + " = ? AND " + MARKER_LONGITUDE + " = ?";
        return (super.delete(MARKER_TABLE, selection, selectionArgs) > 0);
    }

    public boolean updateMarker(Marker marker) {
        setContentValue(marker);
        final String[] selectionArgs = { String.valueOf(marker.getId()) };
        final String selection = MARKER_ID + " = ?";
        return (super.update(MARKER_TABLE, getContentValue(), selection, selectionArgs)>0);
    }

    public Integer addMarker(Marker marker) {
        setContentValue(marker);
        return Math.toIntExact(super.insert(MARKER_TABLE, getContentValue()));
    }

    public Marker cursorToEntity(Cursor c) {
        Marker m = new Marker();
        if (c != null) {
            if (c.getColumnIndex(MARKER_ID) != -1)             {m.setId(c.getInt(c.getColumnIndexOrThrow(MARKER_ID))); }
            if (c.getColumnIndex(MARKER_TRAVEL_ID) != -1)      {m.setTravel_id(c.getInt(c.getColumnIndexOrThrow(MARKER_TRAVEL_ID))); }
            if (c.getColumnIndex(MARKER_ITINERARY_ID) != -1)   {m.setItinerary_id(c.getInt(c.getColumnIndexOrThrow(MARKER_ITINERARY_ID))); }
            if (c.getColumnIndex(MARKER_ACHIEVEMENT_ID) != -1)
                if (c.getInt(c.getColumnIndexOrThrow(MARKER_ACHIEVEMENT_ID)) == 0)
                    m.setAchievement_id(null);
                else
                    m.setAchievement_id(c.getInt(c.getColumnIndexOrThrow(MARKER_ACHIEVEMENT_ID)));
            if (c.getColumnIndex(MARKER_MARKER_TYPE) != -1)    {m.setMarker_type(c.getInt(c.getColumnIndexOrThrow(MARKER_MARKER_TYPE))); }
            if (c.getColumnIndex(MARKER_SEQUENCE) != -1)       {m.setSequence(c.getInt(c.getColumnIndexOrThrow(MARKER_SEQUENCE))); }
            if (c.getColumnIndex(MARKER_NAME) != -1)           {m.setName(c.getString(c.getColumnIndexOrThrow(MARKER_NAME))); }
            if (c.getColumnIndex(MARKER_ADDRESS) != -1)        {m.setAddress(c.getString(c.getColumnIndexOrThrow(MARKER_ADDRESS))); }
            if (c.getColumnIndex(MARKER_CITY) != -1)           {m.setCity(c.getString(c.getColumnIndexOrThrow(MARKER_CITY))); }
            if (c.getColumnIndex(MARKER_STATE) != -1)          {m.setState(c.getString(c.getColumnIndexOrThrow(MARKER_STATE))); }
            if (c.getColumnIndex(MARKER_COUNTRY) != -1)        {m.setCountry(c.getString(c.getColumnIndexOrThrow(MARKER_COUNTRY))); }
            if (c.getColumnIndex(MARKER_ABBR_COUNTRY) != -1)   {m.setAbbr_country(c.getString(c.getColumnIndexOrThrow(MARKER_ABBR_COUNTRY))); }
            if (c.getColumnIndex(MARKER_DESCRIPTION) != -1)    {m.setDescription(c.getString(c.getColumnIndexOrThrow(MARKER_DESCRIPTION))); }
            if (c.getColumnIndex(MARKER_LATITUDE) != -1)       {m.setLatitude(c.getString(c.getColumnIndexOrThrow(MARKER_LATITUDE))); }
            if (c.getColumnIndex(MARKER_LONGITUDE) != -1)      {m.setLongitude(c.getString(c.getColumnIndexOrThrow(MARKER_LONGITUDE))); }
        }
        return m;
    }

    private void setContentValue(Marker m) {
        initialValues = new ContentValues();
        initialValues.put(MARKER_ID, m.id);
        initialValues.put(MARKER_TRAVEL_ID, m.travel_id);
        initialValues.put(MARKER_ITINERARY_ID, m.itinerary_id);
        initialValues.put(MARKER_ACHIEVEMENT_ID, m.achievement_id);
        initialValues.put(MARKER_MARKER_TYPE, m.marker_type);
        initialValues.put(MARKER_SEQUENCE, m.sequence);
        initialValues.put(MARKER_NAME, m.name);
        initialValues.put(MARKER_ADDRESS, m.address);
        initialValues.put(MARKER_CITY, m.city);
        initialValues.put(MARKER_STATE, m.state);
        initialValues.put(MARKER_COUNTRY, m.country);
        initialValues.put(MARKER_ABBR_COUNTRY, m.abbr_country);
        initialValues.put(MARKER_DESCRIPTION, m.description);
        initialValues.put(MARKER_LATITUDE, m.latitude);
        initialValues.put(MARKER_LONGITUDE, m.longitude);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

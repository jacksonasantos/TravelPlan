package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public Marker fetchMarkerById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MARKER_ID + " = ?";
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

    public Cursor fetchMarkerByTravelId(Integer travel_id) {
        //public List<Marker> fetchMarkerByTravelId(Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = MARKER_TRAVEL_ID + " = ?";
        return super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_SEQUENCE);
    }

    public List<Marker> fetchAllMarker() {
        List<Marker> markerList = new ArrayList<>();
        cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, null, null, MARKER_TRAVEL_ID);
        if (cursor.moveToFirst()) {
            do {
                Marker marker = cursorToEntity(cursor);
                markerList.add(marker);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return markerList;
    }

    public boolean deleteMarker(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MARKER_ID + " = ?";
        return (super.delete(MARKER_TABLE, selection, selectionArgs) > 0);
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
        return (super.update(MARKER_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addMarker(Marker marker) {
        setContentValue(marker);
        return (super.insert(MARKER_TABLE, getContentValue()) > 0);
    }

    public Marker cursorToEntity(Cursor c) {
        Marker m = new Marker();
        if (c != null) {
            if (c.getColumnIndex(MARKER_ID) != -1)            {m.setId(c.getInt(c.getColumnIndexOrThrow(MARKER_ID))); }
            if (c.getColumnIndex(MARKER_TRAVEL_ID) != -1)     {m.setTravel_id(c.getInt(c.getColumnIndexOrThrow(MARKER_TRAVEL_ID))); }
            if (c.getColumnIndex(MARKER_SEQUENCE) != -1)      {m.setSequence(c.getInt(c.getColumnIndexOrThrow(MARKER_SEQUENCE))); }
            if (c.getColumnIndex(MARKER_NAME) != -1)          {m.setName(c.getString(c.getColumnIndexOrThrow(MARKER_NAME))); }
            if (c.getColumnIndex(MARKER_ADDRESS) != -1)       {m.setAddress(c.getString(c.getColumnIndexOrThrow(MARKER_ADDRESS))); }
            if (c.getColumnIndex(MARKER_CITY) != -1)          {m.setCity(c.getString(c.getColumnIndexOrThrow(MARKER_CITY))); }
            if (c.getColumnIndex(MARKER_STATE) != -1)         {m.setState(c.getString(c.getColumnIndexOrThrow(MARKER_STATE))); }
            if (c.getColumnIndex(MARKER_COUNTRY) != -1)       {m.setCountry(c.getString(c.getColumnIndexOrThrow(MARKER_COUNTRY))); }
            if (c.getColumnIndex(MARKER_ABBR_COUNTRY) != -1)  {m.setAbbr_country(c.getString(c.getColumnIndexOrThrow(MARKER_ABBR_COUNTRY))); }
            if (c.getColumnIndex(MARKER_CATEGORY_TYPE) != -1) {m.setCategory_type(c.getInt(c.getColumnIndexOrThrow(MARKER_CATEGORY_TYPE))); }
            if (c.getColumnIndex(MARKER_DESCRIPTION) != -1)   {m.setDescription(c.getString(c.getColumnIndexOrThrow(MARKER_DESCRIPTION))); }
            if (c.getColumnIndex(MARKER_LATITUDE) != -1)      {m.setLatitude(c.getString(c.getColumnIndexOrThrow(MARKER_LATITUDE))); }
            if (c.getColumnIndex(MARKER_LONGITUDE) != -1)     {m.setLongitude(c.getString(c.getColumnIndexOrThrow(MARKER_LONGITUDE))); }
            if (c.getColumnIndex(MARKER_ZOOM_LEVEL) != -1)    {m.setZoom_level(c.getString(c.getColumnIndexOrThrow(MARKER_ZOOM_LEVEL))); }
        }
        return m;
    }

    private void setContentValue(Marker m) {
        initialValues = new ContentValues();
        initialValues.put(MARKER_ID, m.id);
        initialValues.put(MARKER_TRAVEL_ID, m.travel_id);
        initialValues.put(MARKER_SEQUENCE, m.sequence);
        initialValues.put(MARKER_NAME, m.name);
        initialValues.put(MARKER_ADDRESS, m.address);
        initialValues.put(MARKER_CITY, m.city);
        initialValues.put(MARKER_STATE, m.state);
        initialValues.put(MARKER_COUNTRY, m.country);
        initialValues.put(MARKER_CATEGORY_TYPE, m.category_type);
        initialValues.put(MARKER_DESCRIPTION, m.description);
        initialValues.put(MARKER_LATITUDE, m.latitude);
        initialValues.put(MARKER_LONGITUDE, m.longitude);
        initialValues.put(MARKER_ZOOM_LEVEL, m.zoom_level);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

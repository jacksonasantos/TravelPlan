package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        //List<Marker> markerList = new ArrayList<>();
        return super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_ID);
       /* cursor = super.query(MARKER_TABLE, MARKER_COLUMNS, selection, selectionArgs, MARKER_ID);
        if (cursor.moveToFirst()) {
            do {
                Marker marker = cursorToEntity(cursor);
                markerList.add(marker);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return markerList;*/
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

    public void deleteMarker(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = MARKER_ID + " = ?";
        super.delete(MARKER_TABLE, selection, selectionArgs);
    }
    public void deleteMarker(Integer travel_id, String latitude, String longitude) {
        final String[] selectionArgs = { String.valueOf(travel_id), latitude, longitude };
        final String selection = MARKER_TRAVEL_ID + " = ? AND " + MARKER_LATITUDE + " = ? AND " + MARKER_LONGITUDE + " = ?";
        super.delete(MARKER_TABLE, selection, selectionArgs);
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

    public Marker cursorToEntity(Cursor cursor) {

        Marker marker = new Marker();

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
            if (cursor.getColumnIndex(MARKER_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(MARKER_ID);
                marker.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(MARKER_TRAVEL_ID) != -1) {
                travel_idIndex = cursor.getColumnIndexOrThrow(MARKER_TRAVEL_ID);
                marker.setTravel_id(cursor.getInt(travel_idIndex));
            }
            if (cursor.getColumnIndex(MARKER_NAME) != -1) {
                nameIndex = cursor.getColumnIndexOrThrow(MARKER_NAME);
                marker.setName(cursor.getString(nameIndex));
            }
            if (cursor.getColumnIndex(MARKER_ADDRESS) != -1) {
                addressIndex = cursor.getColumnIndexOrThrow(MARKER_ADDRESS);
                marker.setAddress(cursor.getString(addressIndex));
            }
            if (cursor.getColumnIndex(MARKER_CATEGORY_TYPE) != -1) {
                category_typeIndex = cursor.getColumnIndexOrThrow(MARKER_CATEGORY_TYPE);
                marker.setCategory_type(cursor.getInt(category_typeIndex));
            }
            if (cursor.getColumnIndex(MARKER_DESCRIPTION) != -1) {
                descriptionIndex = cursor.getColumnIndexOrThrow(MARKER_DESCRIPTION);
                marker.setDescription(cursor.getString(descriptionIndex));
            }
            if (cursor.getColumnIndex(MARKER_LATITUDE) != -1) {
                latitudeIndex = cursor.getColumnIndexOrThrow(MARKER_LATITUDE);
                marker.setLatitude(cursor.getString(latitudeIndex));
            }
            if (cursor.getColumnIndex(MARKER_LONGITUDE) != -1) {
                longitudeIndex = cursor.getColumnIndexOrThrow(MARKER_LONGITUDE);
                marker.setLongitude(cursor.getString(longitudeIndex));
            }
            if (cursor.getColumnIndex(MARKER_ZOOM_LEVEL) != -1) {
                zoom_levelIndex = cursor.getColumnIndexOrThrow(MARKER_ZOOM_LEVEL);
                marker.setZoom_level(cursor.getString(zoom_levelIndex));
            }
        }
        return marker;
    }

    private void setContentValue(Marker marker) {
        initialValues = new ContentValues();
        initialValues.put(MARKER_ID, marker.id);
        initialValues.put(MARKER_TRAVEL_ID, marker.travel_id);
        initialValues.put(MARKER_NAME, marker.name);
        initialValues.put(MARKER_ADDRESS, marker.address);
        initialValues.put(MARKER_CATEGORY_TYPE, marker.category_type);
        initialValues.put(MARKER_DESCRIPTION, marker.description);
        initialValues.put(MARKER_LATITUDE, marker.latitude);
        initialValues.put(MARKER_LONGITUDE, marker.longitude);
        initialValues.put(MARKER_ZOOM_LEVEL, marker.zoom_level);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

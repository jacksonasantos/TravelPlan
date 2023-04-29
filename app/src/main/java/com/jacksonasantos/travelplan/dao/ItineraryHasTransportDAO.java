package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryHasTransportIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.ItineraryHasTransportISchema;

import java.util.ArrayList;
import java.util.List;

public class ItineraryHasTransportDAO extends DbContentProvider implements ItineraryHasTransportISchema, ItineraryHasTransportIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public ItineraryHasTransportDAO(SQLiteDatabase db) {
        super(db);
    }

    public ItineraryHasTransport fetchItineraryHasTransportById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ITINERARY_HAS_TRANSPORT_ID + " = ?";
        ItineraryHasTransport itinerary = new ItineraryHasTransport();
        cursor = super.query(ITINERARY_HAS_TRANSPORT_TABLE, ITINERARY_HAS_TRANSPORT_COLUMNS, selection, selectionArgs, ITINERARY_HAS_TRANSPORT_ID);
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

    public List<ItineraryHasTransport> fetchAllItineraryHasTransportByTravel(Integer travel_id) {
        List<ItineraryHasTransport> itineraryHasTransportList = new ArrayList<>();

        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = ITINERARY_HAS_TRANSPORT_TRAVEL_ID + " = ?";

        cursor = super.query(ITINERARY_HAS_TRANSPORT_TABLE, ITINERARY_HAS_TRANSPORT_COLUMNS, selection, selectionArgs, ITINERARY_HAS_TRANSPORT_SEQUENCE_ITINERARY);

        if (cursor.moveToFirst()) {
            do {
                ItineraryHasTransport it = cursorToEntity(cursor);
                itineraryHasTransportList.add(it);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return itineraryHasTransportList;
    }
    public List<ItineraryHasTransport> fetchAllItineraryHasTransportByTravelType(Integer travel_id, Integer transport_type) {
        List<ItineraryHasTransport> itineraryHasTransportList = new ArrayList<>();

        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(transport_type) };
        final String selection = ITINERARY_HAS_TRANSPORT_TRAVEL_ID + " = ? AND "+ITINERARY_HAS_TRANSPORT_TRANSPORT_TYPE + " = ? ";

        cursor = super.query(ITINERARY_HAS_TRANSPORT_TABLE, ITINERARY_HAS_TRANSPORT_COLUMNS, selection, selectionArgs, ITINERARY_HAS_TRANSPORT_SEQUENCE_ITINERARY);

        if (cursor.moveToFirst()) {
            do {
                ItineraryHasTransport it = cursorToEntity(cursor);
                itineraryHasTransportList.add(it);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return itineraryHasTransportList;
    }

    public List<ItineraryHasTransport> fetchAllItineraryHasTransportByTravelItinerary(Integer travel_id, Integer itinerary_id) {
        List<ItineraryHasTransport> itineraryHasTransportList = new ArrayList<>();

        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(itinerary_id)};
        final String selection = ITINERARY_HAS_TRANSPORT_TRAVEL_ID + " = ? AND "+ITINERARY_HAS_TRANSPORT_ITINERARY_ID + "= ? ";

        cursor = super.query(ITINERARY_HAS_TRANSPORT_TABLE, ITINERARY_HAS_TRANSPORT_COLUMNS, selection, selectionArgs, ITINERARY_HAS_TRANSPORT_SEQUENCE_ITINERARY);

        if (cursor.moveToFirst()) {
            do {
                ItineraryHasTransport it = cursorToEntity(cursor);
                itineraryHasTransportList.add(it);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return itineraryHasTransportList;
    }

    public void deleteItineraryHasTransport(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ITINERARY_HAS_TRANSPORT_ID + " = ?";
        super.delete(ITINERARY_HAS_TRANSPORT_TABLE, selection, selectionArgs);
    }

    public boolean updateItineraryHasTransport(ItineraryHasTransport itinerary) {
        setContentValue(itinerary);
        final String[] selectionArgs = { String.valueOf(itinerary.getId()) };
        final String selection = ITINERARY_HAS_TRANSPORT_ID + " = ?";
        return (super.update(ITINERARY_HAS_TRANSPORT_TABLE, getContentValue(), selection, selectionArgs)>0);
    }

    public boolean addItineraryHasTransport(ItineraryHasTransport itinerary) {
        setContentValue(itinerary);
        return (super.insert(ITINERARY_HAS_TRANSPORT_TABLE, getContentValue()) > 0);
    }

    protected ItineraryHasTransport cursorToEntity(Cursor c) {
        ItineraryHasTransport i = new ItineraryHasTransport();
        if (c != null) {
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_ID) != -1)               {i.setId(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_ID))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_TRAVEL_ID) != -1)        {i.setTravel_id(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_TRAVEL_ID))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_TRANSPORT_ID) != -1)     {i.setTransport_id(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_TRANSPORT_ID))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_VEHICLE_ID) != -1)       {i.setVehicle_id(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_VEHICLE_ID))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_ITINERARY_ID) != -1)     {i.setItinerary_id(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_ITINERARY_ID))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_PERSON_ID) != -1)        {i.setPerson_id(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_PERSON_ID))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_TRANSPORT_TYPE) != -1)   {i.setTransport_type(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_TRANSPORT_TYPE))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_DRIVER) != -1)           {i.setDriver(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_DRIVER))); }
            if (c.getColumnIndex(ITINERARY_HAS_TRANSPORT_SEQUENCE_ITINERARY)!=-1) {i.setSequence_itinerary(c.getInt(c.getColumnIndexOrThrow(ITINERARY_HAS_TRANSPORT_SEQUENCE_ITINERARY))); }
        }
        return i;
    }

    private void setContentValue(ItineraryHasTransport i) {
        initialValues = new ContentValues();
        initialValues.put(ITINERARY_HAS_TRANSPORT_ID, i.id);
        initialValues.put(ITINERARY_HAS_TRANSPORT_TRAVEL_ID, i.travel_id);
        initialValues.put(ITINERARY_HAS_TRANSPORT_TRANSPORT_ID, i.transport_id);
        initialValues.put(ITINERARY_HAS_TRANSPORT_VEHICLE_ID, i.vehicle_id);
        initialValues.put(ITINERARY_HAS_TRANSPORT_ITINERARY_ID, i.itinerary_id);
        initialValues.put(ITINERARY_HAS_TRANSPORT_PERSON_ID, i.person_id);
        initialValues.put(ITINERARY_HAS_TRANSPORT_TRANSPORT_TYPE, i.transport_type);
        initialValues.put(ITINERARY_HAS_TRANSPORT_DRIVER, i.driver);
        initialValues.put(ITINERARY_HAS_TRANSPORT_SEQUENCE_ITINERARY, i.sequence_itinerary);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

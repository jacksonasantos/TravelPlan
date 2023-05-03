package com.jacksonasantos.travelplan.dao;

import static com.jacksonasantos.travelplan.dao.interfaces.TravelISchema.TRAVEL_ID;
import static com.jacksonasantos.travelplan.dao.interfaces.TravelISchema.TRAVEL_TABLE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
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

    public Itinerary fetchItineraryByTravelId(Integer travel_id, int sequence) {
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(sequence) };
        final String selection = ITINERARY_TRAVEL_ID + " = ? AND "+ITINERARY_SEQUENCE + " = ? ";
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

    public Itinerary fetchLastItineraryByTravel(Integer travel_id) {
        Itinerary itinerary = new Itinerary();
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = ITINERARY_TRAVEL_ID + " = ?";

        cursor = super.query(ITINERARY_TABLE, ITINERARY_COLUMNS, selection, selectionArgs, ITINERARY_SEQUENCE + " DESC ");
        if (cursor.moveToFirst()) {
            itinerary = cursorToEntity(cursor);
            cursor.close();
        }
        return itinerary;
    }

    public Cursor fetchArrayItinerary(Integer travel_id){
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = ITINERARY_TRAVEL_ID + " = ?";

        return super.rawQuery("SELECT "+ITINERARY_ID+" _id, "+
                ITINERARY_ORIG_LOCATION+"||' - '||"+ITINERARY_DEST_LOCATION+" text1 "+
                "FROM "+ITINERARY_TABLE + " WHERE "+ selection, selectionArgs);
    }

    public String fetchItineraryDateSequence(Integer travel_id, Integer sequence){
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(travel_id), String.valueOf(sequence) };

        String itineraryDate = null;
        cursor =super.rawQuery("SELECT STRFTIME('%d/%m/%Y', DATE(DATE(t.departure_date, '+'||i." + ITINERARY_DAILY + "||' days'), '-'||ii." + ITINERARY_DAILY + "||' days')) date_sequence " +
                                     "FROM (SELECT i1." + ITINERARY_TRAVEL_ID + ", " +
                                                  "i1." + ITINERARY_SEQUENCE + ", " +
                                                  "SUM(i2." + ITINERARY_DAILY + ") as daily " +
                                             "FROM " + ITINERARY_TABLE + " AS i1 " +
                                            "INNER JOIN " + ITINERARY_TABLE + " AS i2 ON i1." + ITINERARY_TRAVEL_ID + " = i2." + ITINERARY_TRAVEL_ID + " " +
                                                                                    "AND i1." + ITINERARY_SEQUENCE + " >= i2." + ITINERARY_SEQUENCE + " " +
                                            "WHERE i1." + ITINERARY_TRAVEL_ID + " = ? " +
                                            "GROUP BY i1." + ITINERARY_TRAVEL_ID + ", i1." + ITINERARY_SEQUENCE + " " +
                                            "ORDER BY i1." + ITINERARY_SEQUENCE + " ASC ) i " +
                                    "INNER JOIN " + TRAVEL_TABLE + " t ON t." + TRAVEL_ID + " = i." + ITINERARY_TRAVEL_ID + " " +
                                    "INNER JOIN " + ITINERARY_TABLE + " ii ON ii." + ITINERARY_TRAVEL_ID + " = i." + ITINERARY_TRAVEL_ID + " " +
                                                                         "AND ii." + ITINERARY_SEQUENCE + " = i." + ITINERARY_SEQUENCE + " " +
                                    "WHERE i." + ITINERARY_TRAVEL_ID + " = ? " +
                                      "AND i." + ITINERARY_SEQUENCE + " = ? " +
                                    "ORDER BY ii." + ITINERARY_SEQUENCE, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                itineraryDate = cursor.getString(cursor.getColumnIndexOrThrow("date_sequence"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return itineraryDate;
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
        return (super.update(ITINERARY_TABLE, getContentValue(), selection, selectionArgs)>0);
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
            if (c.getColumnIndex(ITINERARY_DISTANCE) != -1)         {i.setDistance(c.getInt(c.getColumnIndexOrThrow(ITINERARY_DISTANCE))); }
            if (c.getColumnIndex(ITINERARY_DAILY) != -1)            {i.setDaily(c.getInt(c.getColumnIndexOrThrow(ITINERARY_DAILY))); }
            if (c.getColumnIndex(ITINERARY_TIME) != -1)             {i.setTime(c.getInt(c.getColumnIndexOrThrow(ITINERARY_TIME))); }
            if (c.getColumnIndex(ITINERARY_TRAVEL_MODE) != -1)      {i.setTravel_mode(c.getInt(c.getColumnIndexOrThrow(ITINERARY_TRAVEL_MODE))); }
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
        initialValues.put(ITINERARY_DAILY, i.daily);
        initialValues.put(ITINERARY_DISTANCE, i.distance);
        initialValues.put(ITINERARY_TIME, i.time);
        initialValues.put(ITINERARY_TRAVEL_MODE, i.travel_mode);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

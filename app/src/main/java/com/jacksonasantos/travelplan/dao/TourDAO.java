package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.TourIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TourISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TourDAO extends DbContentProvider implements TourISchema, TourIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public TourDAO(SQLiteDatabase db) {
        super(db);
    }

    public Tour fetchTourById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TOUR_ID + " = ?";
        Tour tour = new Tour();
        cursor = super.query(TOUR_TABLE, TOUR_COLUMNS, selection, selectionArgs, TOUR_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tour = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tour;
    }

    public List<Tour> fetchAllTourByTravel(Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = TOUR_TRAVEL_ID + " = ?";
        List<Tour> tourList = new ArrayList<>();

        cursor = super.query(TOUR_TABLE, TOUR_COLUMNS, selection, selectionArgs, TOUR_TOUR_DATE );
        if (cursor.moveToFirst()) {
            do {
                Tour tour = cursorToEntity(cursor);
                tourList.add(tour);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return tourList;
    }

    public List<Tour> fetchAllTour() {
        List<Tour> tourList = new ArrayList<>();

        cursor = super.query(TOUR_TABLE, TOUR_COLUMNS, null, null, TOUR_TOUR_DATE );
        if (cursor.moveToFirst()) {
            do {
                Tour tour = cursorToEntity(cursor);
                tourList.add(tour);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return tourList;
    }

    public ArrayList<Tour> fetchArrayTour(){
        ArrayList<Tour> tourList = new ArrayList<>();
        Cursor cursor = super.query(TOUR_TABLE, TOUR_COLUMNS, null,null, TOUR_TOUR_DATE );
        if(cursor != null && cursor.moveToFirst()){
            do{
                Tour tour = cursorToEntity(cursor);
                tourList.add(tour);
            }while(cursor.moveToNext());
        }
        return tourList;
    }

    public void deleteTour(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TOUR_ID + " = ?";
        super.delete(TOUR_TABLE, selection, selectionArgs);
    }

    public boolean updateTour(Tour tour) {
        setContentValue(tour);
        final String[] selectionArgs = { String.valueOf(tour.getId()) };
        final String selection = TOUR_ID + " = ?";
        return (super.update(TOUR_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addTour(Tour tour) {
        setContentValue(tour);
        return (super.insert(TOUR_TABLE, getContentValue()) > 0);
    }

    protected Tour cursorToEntity(Cursor c) {
        Tour t = new Tour();
        if (c != null) {
            if (c.getColumnIndex(TOUR_ID) != -1)              {t.setId(c.getInt(c.getColumnIndexOrThrow(TOUR_ID))); }
            if (c.getColumnIndex(TOUR_TRAVEL_ID) != -1)       {t.setTravel_id(c.getInt(c.getColumnIndexOrThrow(TOUR_TRAVEL_ID))); }
            if (c.getColumnIndex(TOUR_MARKER_ID) != -1)       {t.setMarker_id(c.getInt(c.getColumnIndexOrThrow(TOUR_MARKER_ID))); }
            if (c.getColumnIndex(TOUR_TOUR_TYPE) != -1)       {t.setTour_type(c.getInt(c.getColumnIndexOrThrow(TOUR_TOUR_TYPE))); }
            if (c.getColumnIndex(TOUR_LOCAL_TOUR) != -1)      {t.setLocal_tour(c.getString(c.getColumnIndexOrThrow(TOUR_LOCAL_TOUR))); }
            if (c.getColumnIndex(TOUR_CURRENCY_TYPE) != -1)   {t.setCurrency_type(c.getInt(c.getColumnIndexOrThrow(TOUR_CURRENCY_TYPE))); }
            if (c.getColumnIndex(TOUR_VALUE_ADULT) != -1)     {t.setValue_adult(c.getDouble(c.getColumnIndexOrThrow(TOUR_VALUE_ADULT))); }
            if (c.getColumnIndex(TOUR_VALUE_CHILD) != -1)     {t.setValue_child(c.getDouble(c.getColumnIndexOrThrow(TOUR_VALUE_CHILD))); }
            if (c.getColumnIndex(TOUR_TOUR_DATE) != -1)       {t.setTour_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TOUR_TOUR_DATE)))); }
            if (c.getColumnIndex(TOUR_OPENING_HOURS) != -1)   {t.setOpening_hours(c.getString(c.getColumnIndexOrThrow(TOUR_OPENING_HOURS))); }
            if (c.getColumnIndex(TOUR_DISTANCE) != -1)        {t.setDistance(c.getInt(c.getColumnIndexOrThrow(TOUR_DISTANCE))); }
            if (c.getColumnIndex(TOUR_VISITATION_TIME) != -1) {t.setVisitation_time(c.getString(c.getColumnIndexOrThrow(TOUR_VISITATION_TIME))); }
            if (c.getColumnIndex(TOUR_NOTE) != -1)            {t.setNote(c.getString(c.getColumnIndexOrThrow(TOUR_NOTE))); }
        }
        return t;
    }

    private void setContentValue(Tour t) {
        initialValues = new ContentValues();
        initialValues.put(TOUR_ID, t.id);
        initialValues.put(TOUR_TRAVEL_ID, t.travel_id);
        initialValues.put(TOUR_MARKER_ID, t.marker_id);
        initialValues.put(TOUR_TOUR_TYPE, t.tour_type);
        initialValues.put(TOUR_LOCAL_TOUR, t.local_tour);
        initialValues.put(TOUR_CURRENCY_TYPE, t.currency_type);
        initialValues.put(TOUR_VALUE_ADULT, t.value_adult);
        initialValues.put(TOUR_VALUE_CHILD, t.value_child);
        initialValues.put(TOUR_TOUR_DATE, Utils.dateFormat(t.tour_date));
        initialValues.put(TOUR_OPENING_HOURS, t.opening_hours);
        initialValues.put(TOUR_DISTANCE, t.distance);
        initialValues.put(TOUR_VISITATION_TIME, t.visitation_time);
        initialValues.put(TOUR_NOTE, t.note);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.interfaces.TravelIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TravelISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TravelDAO extends DbContentProvider implements TravelISchema, TravelIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public TravelDAO(SQLiteDatabase db) {
        super(db);
    }

    public Travel fetchTravelById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_ID + " = ?";
        Travel travel = new Travel();
        cursor = super.query(TRAVEL_TABLE, TRAVEL_COLUMNS, selection, selectionArgs, TRAVEL_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                travel = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return travel;
    }

    public Travel fetchTravelByStatus(int status) {
        final String[] selectionArgs = { String.valueOf(status) };
        final String selection = TRAVEL_STATUS + " = ?";
        Travel travel = new Travel();
        cursor = super.query(TRAVEL_TABLE, TRAVEL_COLUMNS, selection, selectionArgs, TRAVEL_DEPARTURE_DATE);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                travel = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return travel;
    }
    public List<Travel> fetchAllTravel() {
        List<Travel> travelList = new ArrayList<>();
        cursor = super.query(TRAVEL_TABLE, TRAVEL_COLUMNS, null, null, TRAVEL_DEPARTURE_DATE);
        if (cursor.moveToFirst()) {
            do {
                Travel travel = cursorToEntity(cursor);
                travelList.add(travel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return travelList;
    }

    public ArrayList<Travel> fetchArrayTravel(){
        ArrayList<Travel> travelList = new ArrayList<>();
        Cursor cursor = super.query(TRAVEL_TABLE, TRAVEL_COLUMNS, null,null, TRAVEL_DESCRIPTION);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Travel travel = cursorToEntity(cursor);
                travelList.add(travel);
            }while(cursor.moveToNext());
        }
        return travelList;
    }

    public void deleteTravel(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_ID + " = ?";
        super.delete(TRAVEL_TABLE, selection, selectionArgs);
    }

    public boolean updateTravel(Travel travel) {
        setContentValue(travel);
        final String[] selectionArgs = { String.valueOf(travel.getId()) };
        final String selection = TRAVEL_ID + " = ?";
        try {
            return (super.update(TRAVEL_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.w("Update Table", Objects.requireNonNull(ex.getMessage()));
            }
            return false;
        }
    }

    public boolean addTravel(Travel travel) {
        setContentValue(travel);
        try {
            return (super.insert(TRAVEL_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            }
            return false;
        }
    }

    protected Travel cursorToEntity(Cursor cursor) {

        Travel travel = new Travel();

        int idIndex;
        int descriptionIndex;
        int departure_dateIndex;
        int return_dateIndex;
        int noteIndex;
        int statusIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(TRAVEL_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(TRAVEL_ID);
                travel.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_DESCRIPTION) != -1) {
                descriptionIndex = cursor.getColumnIndexOrThrow(TRAVEL_DESCRIPTION);
                travel.setDescription(cursor.getString(descriptionIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_DEPARTURE_DATE) != -1) {
                departure_dateIndex = cursor.getColumnIndexOrThrow(TRAVEL_DEPARTURE_DATE);
                travel.setDeparture_date(Utils.stringToDate(cursor.getString(departure_dateIndex)));
            }
            if (cursor.getColumnIndex(TRAVEL_RETURN_DATE) != -1) {
                return_dateIndex = cursor.getColumnIndexOrThrow(TRAVEL_RETURN_DATE);
                travel.setReturn_date(Utils.stringToDate(cursor.getString(return_dateIndex)));
            }
            if (cursor.getColumnIndex(TRAVEL_NOTE) != -1) {
                noteIndex = cursor.getColumnIndexOrThrow(TRAVEL_NOTE);
                travel.setNote(cursor.getString(noteIndex));
            }
            if (cursor.getColumnIndex(TRAVEL_STATUS) != -1 ){
                statusIndex = cursor.getColumnIndexOrThrow(TRAVEL_STATUS);
                travel.setStatus(cursor.getInt(statusIndex));
            }
        }
        return travel;
    }

    private void setContentValue(Travel travel) {
        initialValues = new ContentValues();
        initialValues.put(TRAVEL_ID, travel.id);
        initialValues.put(TRAVEL_DESCRIPTION, travel.description);
        initialValues.put(TRAVEL_DEPARTURE_DATE, Utils.dateToString(travel.departure_date));
        initialValues.put(TRAVEL_RETURN_DATE, Utils.dateToString(travel.return_date));
        initialValues.put(TRAVEL_NOTE, travel.note);
        initialValues.put(TRAVEL_STATUS, travel.status);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

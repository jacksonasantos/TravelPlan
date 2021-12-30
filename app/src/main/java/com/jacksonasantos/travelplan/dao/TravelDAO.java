package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.TravelIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TravelISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

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

    public Cursor selectTravels(){
        String[] TRAVEL_COLUMNS = new String[] {
                TRAVEL_ID,
                TRAVEL_DESCRIPTION
        };
        return super.query(TRAVEL_TABLE, TRAVEL_COLUMNS, null,null, TRAVEL_DESCRIPTION);
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

        cursor = super.query(TRAVEL_TABLE, TRAVEL_COLUMNS, null, null, TRAVEL_DEPARTURE_DATE + " DESC");
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
        return (super.update(TRAVEL_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addTravel(Travel travel) {
        setContentValue(travel);
        return (super.insert(TRAVEL_TABLE, getContentValue()) > 0);
    }

    protected Travel cursorToEntity(Cursor c) {
        Travel t = new Travel();
        if (c != null) {
            if (c.getColumnIndex(TRAVEL_ID) != -1)              {t.setId(c.getInt(c.getColumnIndexOrThrow(TRAVEL_ID))); }
            if (c.getColumnIndex(TRAVEL_DESCRIPTION) != -1)     {t.setDescription(c.getString(c.getColumnIndexOrThrow(TRAVEL_DESCRIPTION))); }
            if (c.getColumnIndex(TRAVEL_DEPARTURE_DATE) != -1)  {t.setDeparture_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TRAVEL_DEPARTURE_DATE)))); }
            if (c.getColumnIndex(TRAVEL_RETURN_DATE) != -1)     {t.setReturn_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TRAVEL_RETURN_DATE)))); }
            if (c.getColumnIndex(TRAVEL_NOTE) != -1)            {t.setNote(c.getString(c.getColumnIndexOrThrow(TRAVEL_NOTE))); }
            if (c.getColumnIndex(TRAVEL_STATUS) != -1 )         {t.setStatus(c.getInt(c.getColumnIndexOrThrow(TRAVEL_STATUS))); }
        }
        return t;
    }

    private void setContentValue(Travel t) {
        initialValues = new ContentValues();
        initialValues.put(TRAVEL_ID, t.id);
        initialValues.put(TRAVEL_DESCRIPTION, t.description);
        initialValues.put(TRAVEL_DEPARTURE_DATE, Utils.dateFormat(t.departure_date));
        initialValues.put(TRAVEL_RETURN_DATE, Utils.dateFormat(t.return_date));
        initialValues.put(TRAVEL_NOTE, t.note);
        initialValues.put(TRAVEL_STATUS, t.status);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

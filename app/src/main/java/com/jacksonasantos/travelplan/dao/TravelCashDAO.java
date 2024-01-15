package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.TravelCashIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TravelCashISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TravelCashDAO extends DbContentProvider implements TravelCashISchema, TravelCashIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public TravelCashDAO(SQLiteDatabase db) {
        super(db);
    }

    public TravelCash fetchTravelCashById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_CASH_ID + " = ?";
        TravelCash TravelCash = new TravelCash();
        cursor = super.query(TRAVEL_CASH_TABLE, TRAVEL_CASH_COLUMNS, selection, selectionArgs, TRAVEL_CASH_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TravelCash = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return TravelCash;
    }

    public List<TravelCash> fetchAllTravelCashByTravel(Integer travel_id) {
        List<TravelCash> TravelCashList = new ArrayList<>();
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = TRAVEL_CASH_TRAVEL_ID + " = ?";
        cursor = super.query(TRAVEL_CASH_TABLE, TRAVEL_CASH_COLUMNS, selection, selectionArgs, TRAVEL_CASH_CASH_DEPOSIT);
        if (cursor.moveToFirst()) {
            do {
                TravelCash TravelCash = cursorToEntity(cursor);
                TravelCashList.add(TravelCash);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return TravelCashList;
    }

    public List<TravelCash> fetchAllTravelCash() {
        List<TravelCash> TravelCashList = new ArrayList<>();

        cursor = super.query(TRAVEL_CASH_TABLE, TRAVEL_CASH_COLUMNS, null,null, TRAVEL_CASH_CASH_DEPOSIT);

        if (cursor.moveToFirst()) {
            do {
                TravelCash TravelCash = cursorToEntity(cursor);
                TravelCashList.add(TravelCash);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return TravelCashList;
    }

    public void deleteTravelCash(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_CASH_ID + " = ?";
        super.delete(TRAVEL_CASH_TABLE, selection, selectionArgs);
    }

    public boolean updateTravelCash(TravelCash TravelCash) {
        setContentValue(TravelCash);
        final String[] selectionArgs = { String.valueOf(TravelCash.getId()) };
        final String selection = TRAVEL_CASH_ID + " = ?";
        return (super.update(TRAVEL_CASH_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addTravelCash(TravelCash TravelCash) {
        setContentValue(TravelCash);
        return (super.insert(TRAVEL_CASH_TABLE, getContentValue()) > 0);
    }

    protected TravelCash cursorToEntity(Cursor c) {
        TravelCash tc = new TravelCash();
        if (c != null) {
            if (c.getColumnIndex(TRAVEL_CASH_ID) != -1)                  {tc.id = c.getInt(c.getColumnIndexOrThrow(TRAVEL_CASH_ID)); }
            if (c.getColumnIndex(TRAVEL_CASH_TRAVEL_ID) != -1)           {tc.travel_id = c.getInt(c.getColumnIndexOrThrow(TRAVEL_CASH_TRAVEL_ID)); }
            if (c.getColumnIndex(TRAVEL_CASH_CURRENCY_ID) != -1)         {tc.currency_id = c.getInt(c.getColumnIndexOrThrow(TRAVEL_CASH_CURRENCY_ID)); }
            if (c.getColumnIndex(TRAVEL_CASH_CASH_DEPOSIT) != -1)        {tc.cash_deposit = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TRAVEL_CASH_CASH_DEPOSIT))); }
            if (c.getColumnIndex(TRAVEL_CASH_AMOUNT_DEPOSIT) != -1)      {tc.amount_deposit = c.getDouble(c.getColumnIndexOrThrow(TRAVEL_CASH_AMOUNT_DEPOSIT)); }
        }
        return tc;
    }

    private void setContentValue(TravelCash tc) {
        initialValues = new ContentValues();
        initialValues.put(TRAVEL_CASH_ID, tc.id);
        initialValues.put(TRAVEL_CASH_TRAVEL_ID, tc.travel_id);
        initialValues.put(TRAVEL_CASH_CURRENCY_ID, tc.currency_id);
        initialValues.put(TRAVEL_CASH_CASH_DEPOSIT, Utils.dateFormat(tc.cash_deposit));
        initialValues.put(TRAVEL_CASH_AMOUNT_DEPOSIT, tc.amount_deposit);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

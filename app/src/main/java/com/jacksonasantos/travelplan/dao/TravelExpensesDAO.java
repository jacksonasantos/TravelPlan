package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.TourISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TravelExpensesIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TravelExpensesISchema;

import java.util.ArrayList;
import java.util.List;

public class TravelExpensesDAO extends DbContentProvider implements TravelExpensesISchema, TravelExpensesIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public TravelExpensesDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<TravelExpenses> fetchAllTravelExpensesByTravel( Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = TRAVEL_EXPENSES_TRAVEL_ID + " = ?";
        List<TravelExpenses> travelExpensesList = new ArrayList<>();

        cursor = super.query(TRAVEL_EXPENSES_TABLE, TRAVEL_EXPENSES_COLUMNS, selection, selectionArgs, TRAVEL_EXPENSES_EXPENSE_TYPE);
        if (cursor.moveToFirst()) {
            do {
                TravelExpenses travelExpenses = cursorToEntity(cursor);
                travelExpensesList.add(travelExpenses);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return travelExpensesList;
    }

    public List<TravelExpenses> fetchAllTravelExpensesByTravelType(Integer travel_id, Integer expense_type) {
        List<TravelExpenses> travelExpensesList = new ArrayList<>();
        String str_te = "SELECT te." + TravelExpensesISchema.TRAVEL_EXPENSES_ID + " " + TravelExpensesISchema.TRAVEL_EXPENSES_ID + ", " +
                              " te." + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + " " + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + ", " +
                              " te." + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + ", " +
                              " te." + TravelExpensesISchema.TRAVEL_EXPENSES_EXPECTED_VALUE + " " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPECTED_VALUE + ", " +
                              " te." + TravelExpensesISchema.TRAVEL_EXPENSES_NOTE + " " + TravelExpensesISchema.TRAVEL_EXPENSES_NOTE + ", " +
                              " te." + TravelExpensesISchema.TRAVEL_EXPENSES_MARKER_ID + " " + TravelExpensesISchema.TRAVEL_EXPENSES_MARKER_ID +
                         " FROM " + TravelExpensesISchema.TRAVEL_EXPENSES_TABLE + " te " +
                        " WHERE " + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + " = ? " +
                          " AND te." + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + " = ? ";
        String str_tour = "";
        if(expense_type == 3) {
            str_tour = " UNION "+
                    //" SELECT t." + TourISchema.TOUR_ID + " " + TravelExpensesISchema.TRAVEL_EXPENSES_ID + ", " +
                    " SELECT 0 " + TravelExpensesISchema.TRAVEL_EXPENSES_ID + ", " +
                             " t." + TourISchema.TOUR_TRAVEL_ID + " " + TravelExpensesISchema.TRAVEL_EXPENSES_TRAVEL_ID + ", " +
                             " 3 " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPENSE_TYPE + ", " +
                             " (t." + TourISchema.TOUR_NUMBER_ADULT + " * t." + TourISchema.TOUR_VALUE_ADULT + ") + (t." + TourISchema.TOUR_NUMBER_CHILD + " * t." + TourISchema.TOUR_VALUE_CHILD + ") " + TravelExpensesISchema.TRAVEL_EXPENSES_EXPECTED_VALUE + ", " +
                             " t." + TourISchema.TOUR_NOTE + " " + TravelExpensesISchema.TRAVEL_EXPENSES_NOTE + ", " +
                             " t." + TourISchema.TOUR_MARKER_ID + " " + TravelExpensesISchema.TRAVEL_EXPENSES_MARKER_ID +
                        " FROM " + TourISchema.TOUR_TABLE + " t " +
                       " WHERE " + TourISchema.TOUR_TRAVEL_ID + " = " + travel_id;
        }
        cursor = super.rawQuery( str_te + str_tour,
                new String[] { String.valueOf(travel_id), String.valueOf(expense_type)});
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    TravelExpenses travelExpenses = cursorToEntity(cursor);
                    travelExpensesList.add(travelExpenses);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return travelExpensesList;
    }

    public TravelExpenses fetchTravelExpensesById( Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_EXPENSES_ID + " = ? ";
        TravelExpenses travelExpenses = new TravelExpenses();

        cursor = super.query(TRAVEL_EXPENSES_TABLE, TRAVEL_EXPENSES_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                travelExpenses = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return travelExpenses;
    }

    public TravelExpenses fetchTravelExpensesByTravelMarker( Integer travel_id, Integer marker_id) {
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(marker_id) };
        final String selection = TRAVEL_EXPENSES_TRAVEL_ID + " = ? AND " + TRAVEL_EXPENSES_MARKER_ID + " = ? ";

        TravelExpenses travelExpenses = new TravelExpenses();
        cursor = super.query(TRAVEL_EXPENSES_TABLE, TRAVEL_EXPENSES_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                travelExpenses = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return travelExpenses;
    }

    public boolean deleteTravelExpenses(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_EXPENSES_ID + " = ?";
        return (super.delete(TRAVEL_EXPENSES_TABLE, selection, selectionArgs) > 0);
    }


    public boolean updateTravelExpenses(TravelExpenses travelExpenses) {
        setContentValue(travelExpenses);
        final String[] selectionArgs = { String.valueOf(travelExpenses.getId()) };
        final String selection = TRAVEL_EXPENSES_ID + " = ?";
        return (super.update(TRAVEL_EXPENSES_TABLE, getContentValue(), selection, selectionArgs) >0);
    }

    public boolean addTravelExpenses(TravelExpenses travelExpenses) {
        setContentValue(travelExpenses);
        return (super.insert(TRAVEL_EXPENSES_TABLE, getContentValue()) > 0);
    }

    protected TravelExpenses cursorToEntity(Cursor c) {
        TravelExpenses t = new TravelExpenses();
        if (c != null) {
            if (c.getColumnIndex(TRAVEL_EXPENSES_ID) != -1)             {t.setId(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_ID))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_TRAVEL_ID) != -1)      if (c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_TRAVEL_ID)) == 0) t.setTravel_id(null);
                                                                        else t.setTravel_id(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_TRAVEL_ID)));
            if (c.getColumnIndex(TRAVEL_EXPENSES_EXPENSE_TYPE) != -1)   {t.setExpense_type(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_EXPENSE_TYPE))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_EXPECTED_VALUE) != -1) {t.setExpected_value(c.getDouble(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_EXPECTED_VALUE))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_NOTE) != -1 )          {t.setNote(c.getString(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_NOTE))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_MARKER_ID) != -1)      if (c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_MARKER_ID)) == 0) t.setMarker_id(null);
                                                                        else t.setMarker_id(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_MARKER_ID)));
        }
        return t;
    }

    private void setContentValue(TravelExpenses t) {
        initialValues = new ContentValues();
        initialValues.put(TRAVEL_EXPENSES_ID, t.id);
        initialValues.put(TRAVEL_EXPENSES_TRAVEL_ID, t.travel_id);
        initialValues.put(TRAVEL_EXPENSES_EXPENSE_TYPE, t.expense_type);
        initialValues.put(TRAVEL_EXPENSES_EXPECTED_VALUE, t.expected_value);
        initialValues.put(TRAVEL_EXPENSES_NOTE, t.note);
        initialValues.put(TRAVEL_EXPENSES_MARKER_ID, t.marker_id);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

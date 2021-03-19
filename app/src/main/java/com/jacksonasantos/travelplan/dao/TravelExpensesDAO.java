package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public TravelExpenses fetchTravelExpensesById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_EXPENSES_ID + " = ?";
        TravelExpenses travelExpenses = new TravelExpenses();
        cursor = super.query(TRAVEL_EXPENSES_TABLE, TRAVEL_EXPENSES_COLUMNS, selection, selectionArgs, TRAVEL_EXPENSES_ID);
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

    public List<TravelExpenses> fetchAllTravelExpensesByTravelType( Integer travel_id, Integer expense_type) {
        final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(expense_type) };
        final String selection = TRAVEL_EXPENSES_TRAVEL_ID + " = ? AND " + TRAVEL_EXPENSES_EXPENSE_TYPE + " = ? ";
        List<TravelExpenses> travelExpensesList = new ArrayList<>();

        cursor = super.query(TRAVEL_EXPENSES_TABLE, TRAVEL_EXPENSES_COLUMNS, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            do {
                TravelExpenses travelExpenses = cursorToEntity(cursor);
                travelExpensesList.add(travelExpenses);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return travelExpensesList;
    }

    public List<TravelExpenses> fetchAllTravelExpenses() {
        List<TravelExpenses> travelExpensesList = new ArrayList<>();

        cursor = super.query(TRAVEL_EXPENSES_TABLE, TRAVEL_EXPENSES_COLUMNS, null, null, TRAVEL_EXPENSES_EXPENSE_TYPE);
        if (cursor.moveToFirst()) {
            do {
                TravelExpenses travelExpenses = cursorToEntity(cursor);
                travelExpensesList.add(travelExpenses);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return travelExpensesList;
    }

    public void deleteTravelExpenses(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_EXPENSES_ID + " = ?";
        super.delete(TRAVEL_EXPENSES_TABLE, selection, selectionArgs);
    }

    public boolean updateTravelExpenses(TravelExpenses travelExpenses) {
        setContentValue(travelExpenses);
        final String[] selectionArgs = { String.valueOf(travelExpenses.getId()) };
        final String selection = TRAVEL_EXPENSES_ID + " = ?";
        return (super.update(TRAVEL_EXPENSES_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addTravelExpenses(TravelExpenses travelExpenses) {
        setContentValue(travelExpenses);
        return (super.insert(TRAVEL_EXPENSES_TABLE, getContentValue()) > 0);
    }

    protected TravelExpenses cursorToEntity(Cursor c) {
        TravelExpenses t = new TravelExpenses();
        if (c != null) {
            if (c.getColumnIndex(TRAVEL_EXPENSES_ID) != -1)             {t.setId(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_ID))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_TRAVEL_ID) != -1)      {t.setTravel_id(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_TRAVEL_ID))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_EXPENSE_TYPE) != -1)   {t.setExpense_type(c.getInt(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_EXPENSE_TYPE))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_EXPECTED_VALUE) != -1) {t.setExpected_value(c.getDouble(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_EXPECTED_VALUE))); }
            if (c.getColumnIndex(TRAVEL_EXPENSES_NOTE) != -1 )          {t.setNote(c.getString(c.getColumnIndexOrThrow(TRAVEL_EXPENSES_NOTE))); }
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
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

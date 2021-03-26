package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.TravelItemExpensesIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TravelItemExpensesISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TravelItemExpensesDAO extends DbContentProvider implements TravelItemExpensesISchema, TravelItemExpensesIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public TravelItemExpensesDAO(SQLiteDatabase db) {
        super(db);
    }

    public TravelItemExpenses fetchTravelItemExpensesById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_ITEM_EXPENSES_ID + " = ?";
        TravelItemExpenses travelItemExpenses = new TravelItemExpenses();
        cursor = super.query(TRAVEL_ITEM_EXPENSES_TABLE, TRAVEL_ITEM_EXPENSES_COLUMNS, selection, selectionArgs, TRAVEL_ITEM_EXPENSES_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                travelItemExpenses = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return travelItemExpenses;
    }

    public List<TravelItemExpenses> fetchTravelItemExpensesByTravelExpenseId(Integer travel_expense_id) {
        final String[] selectionArgs = { String.valueOf(travel_expense_id) };
        final String selection = TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID + " = ?";
        List<TravelItemExpenses> travelItemExpensesList = new ArrayList<>();
        cursor = super.query(TRAVEL_ITEM_EXPENSES_TABLE, TRAVEL_ITEM_EXPENSES_COLUMNS, selection, selectionArgs, TRAVEL_ITEM_EXPENSES_EXPENSE_DATE+", "+TRAVEL_ITEM_EXPENSES_ID);
        if (cursor.moveToFirst()) {
            do {
                TravelItemExpenses travelItemExpenses = cursorToEntity(cursor);
                travelItemExpensesList.add(travelItemExpenses);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return travelItemExpensesList;
    }

    public List<TravelItemExpenses> fetchAllTravelItemExpenses() {
        List<TravelItemExpenses> travelItemExpensesList = new ArrayList<>();

        cursor = super.query(TRAVEL_ITEM_EXPENSES_TABLE, TRAVEL_ITEM_EXPENSES_COLUMNS, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TravelItemExpenses travelItemExpenses = cursorToEntity(cursor);
                travelItemExpensesList.add(travelItemExpenses);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return travelItemExpensesList;
    }

    public void deleteTravelItemExpenses(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRAVEL_ITEM_EXPENSES_ID + " = ?";
        super.delete(TRAVEL_ITEM_EXPENSES_TABLE, selection, selectionArgs);
    }

    public boolean updateTravelItemExpenses(TravelItemExpenses travelItemExpenses) {
        setContentValue(travelItemExpenses);
        final String[] selectionArgs = { String.valueOf(travelItemExpenses.getId()) };
        final String selection = TRAVEL_ITEM_EXPENSES_ID + " = ?";
        return (super.update(TRAVEL_ITEM_EXPENSES_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addTravelItemExpenses(TravelItemExpenses travelItemExpenses) {
        setContentValue(travelItemExpenses);
        return (super.insert(TRAVEL_ITEM_EXPENSES_TABLE, getContentValue()) > 0);
    }

    protected TravelItemExpenses cursorToEntity(Cursor c) {
        TravelItemExpenses t = new TravelItemExpenses();
        if (c != null) {
            if (c.getColumnIndex(TRAVEL_ITEM_EXPENSES_ID) != -1)                 {t.setId(c.getInt(c.getColumnIndexOrThrow(TRAVEL_ITEM_EXPENSES_ID))); }
            if (c.getColumnIndex(TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID) != -1) {t.setTravel_expense_id(c.getInt(c.getColumnIndexOrThrow(TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID))); }
            if (c.getColumnIndex(TRAVEL_ITEM_EXPENSES_EXPENSE_DATE) != -1)       {t.setExpense_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TRAVEL_ITEM_EXPENSES_EXPENSE_DATE)))); }
            if (c.getColumnIndex(TRAVEL_ITEM_EXPENSES_REALIZED_VALUE) != -1)     {t.setRealized_value(c.getDouble(c.getColumnIndexOrThrow(TRAVEL_ITEM_EXPENSES_REALIZED_VALUE))); }
            if (c.getColumnIndex(TRAVEL_ITEM_EXPENSES_NOTE) != -1 )              {t.setNote(c.getString(c.getColumnIndexOrThrow(TRAVEL_ITEM_EXPENSES_NOTE))); }
        }
        return t;
    }

    private void setContentValue(TravelItemExpenses t) {
        initialValues = new ContentValues();
        initialValues.put(TRAVEL_ITEM_EXPENSES_ID, t.id);
        initialValues.put(TRAVEL_ITEM_EXPENSES_TRAVEL_EXPENSE_ID, t.travel_expense_id);
        initialValues.put(TRAVEL_ITEM_EXPENSES_EXPENSE_DATE,  Utils.dateFormat(t.expense_date));
        initialValues.put(TRAVEL_ITEM_EXPENSES_REALIZED_VALUE, t.realized_value);
        initialValues.put(TRAVEL_ITEM_EXPENSES_NOTE, t.note);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

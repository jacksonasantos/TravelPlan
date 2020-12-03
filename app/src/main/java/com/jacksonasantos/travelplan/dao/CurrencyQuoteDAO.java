package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrencyQuoteDAO extends DbContentProvider implements CurrencyQuoteISchema, CurrencyQuoteIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public CurrencyQuoteDAO(SQLiteDatabase db) {
        super(db);
    }

    public CurrencyQuote findQuoteDay(int currency_type, Date quote_date) {

        CurrencyQuote currencyQuote = new CurrencyQuote();
        if (quote_date == null ) {
            return currencyQuote;
        }
        cursor = super.rawQuery("SELECT rowid, * " +
                                     " FROM " + CURRENCY_QUOTE_TABLE +
                                    " WHERE " + CURRENCY_QUOTE_CURRENCY_TYPE + "=? " +
                                      " AND " + CURRENCY_QUOTE_QUOTE_DATE + "=?",
                new String[] { String.valueOf(currency_type),  Utils.dateToString(quote_date)});
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                currencyQuote = cursorToEntity(cursor);
            }
        cursor.close();
        }
        return currencyQuote;
    }

    public CurrencyQuote fetchCurrencyQuoteById(Long id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        CurrencyQuote currencyQuote = new CurrencyQuote();
        cursor = super.query(CURRENCY_QUOTE_TABLE, CURRENCY_QUOTE_COLUMNS, selection, selectionArgs, CURRENCY_QUOTE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                currencyQuote = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return currencyQuote;
    }

    public List<CurrencyQuote> fetchAllCurrencyQuotes() {
        List<CurrencyQuote> currencyQuoteList = new ArrayList<>();

        cursor = super.query(CURRENCY_QUOTE_TABLE, CURRENCY_QUOTE_COLUMNS, null,null, CURRENCY_QUOTE_QUOTE_DATE);

        if (cursor.moveToFirst()) {
            do {
                CurrencyQuote currencyQuote = cursorToEntity(cursor);
                currencyQuoteList.add(currencyQuote);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return currencyQuoteList;
    }

    public void deleteCurrencyQuote(Long id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        try {
            super.delete(CURRENCY_QUOTE_TABLE, selection, selectionArgs);
        } catch (SQLiteConstraintException ex){
            Log.w("Delete Table", ex.getMessage());
        }
    }

    public boolean updateCurrencyQuote(CurrencyQuote currencyQuote) {
        setContentValue(currencyQuote);
        final String[] selectionArgs = { String.valueOf(currencyQuote.getId()) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        try {
            return (super.update(CURRENCY_QUOTE_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addCurrencyQuote(CurrencyQuote currencyQuote) {
        setContentValue(currencyQuote);
        try {
            return (super.insert(CURRENCY_QUOTE_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", ex.getMessage());
            return false;
        }
    }

    protected CurrencyQuote cursorToEntity(Cursor cursor) {

        CurrencyQuote currencyQuote = new CurrencyQuote();

        int idIndex;
        int currency_typeIndex;
        int quote_dateIndex;
        int currency_valueIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(CURRENCY_QUOTE_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(CURRENCY_QUOTE_ID);
                currencyQuote.id = cursor.getLong(idIndex);
            }
            if (cursor.getColumnIndex(CURRENCY_QUOTE_CURRENCY_TYPE) != -1) {
                currency_typeIndex = cursor.getColumnIndexOrThrow(CURRENCY_QUOTE_CURRENCY_TYPE);
                currencyQuote.currency_type = cursor.getInt(currency_typeIndex);
            }
            if (cursor.getColumnIndex(CURRENCY_QUOTE_QUOTE_DATE) != -1) {
                quote_dateIndex = cursor.getColumnIndexOrThrow(CURRENCY_QUOTE_QUOTE_DATE);
                currencyQuote.quote_date = Utils.stringToDate(cursor.getString(quote_dateIndex));
            }
            if (cursor.getColumnIndex(CURRENCY_QUOTE_CURRENCY_VALUE) != -1) {
                currency_valueIndex = cursor.getColumnIndexOrThrow(CURRENCY_QUOTE_CURRENCY_VALUE);
                currencyQuote.currency_value = cursor.getDouble(currency_valueIndex);
            }
        }
        return currencyQuote;
    }

    private void setContentValue(CurrencyQuote currencyQuote) {
        initialValues = new ContentValues();
        initialValues.put(CURRENCY_QUOTE_ID, currencyQuote.id);
        initialValues.put(CURRENCY_QUOTE_CURRENCY_TYPE, currencyQuote.currency_type);
        initialValues.put(CURRENCY_QUOTE_QUOTE_DATE, Utils.dateToString(currencyQuote.quote_date));
        initialValues.put(CURRENCY_QUOTE_CURRENCY_VALUE, currencyQuote.currency_value);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

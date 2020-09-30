package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuotelDAO;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuotelSchema;

import java.util.ArrayList;
import java.util.List;

public class CurrencyQuoteDAO extends DbContentProvider implements CurrencyQuotelSchema, CurrencyQuotelDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public CurrencyQuoteDAO(SQLiteDatabase db) {
        super(db);
    }

    public CurrencyQuote fetchCurrencyQuoteById(int id) {
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

        cursor = super.query(CURRENCY_QUOTE_TABLE, CURRENCY_QUOTE_COLUMNS, null,null, CURRENCY_QUOTE_ID);

        if (cursor.moveToFirst()) {
            do {
                CurrencyQuote currencyQuote = cursorToEntity(cursor);
                currencyQuoteList.add(currencyQuote);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return currencyQuoteList;
    }

    public boolean deleteCurrencyQuote(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        try {
            return super.delete(CURRENCY_QUOTE_TABLE, selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Delete Table", ex.getMessage());
            return false;
        }
    }

    public boolean updateCurrencyQuote(CurrencyQuote currencyQuote) {
        setContentValue(currencyQuote);
        final String[] selectionArgs = { String.valueOf(currencyQuote.getId()) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        try {
            return super.update(CURRENCY_QUOTE_TABLE, getContentValue(), selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addCurrencyQuote(CurrencyQuote currencyQuote) {
        setContentValue(currencyQuote);
        try {
            return super.insert(CURRENCY_QUOTE_TABLE, getContentValue()) > 0;
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
                currencyQuote.currency_type = cursor.getString(currency_typeIndex);
            }
            if (cursor.getColumnIndex(CURRENCY_QUOTE_QUOTE_DATE) != -1) {
                quote_dateIndex = cursor.getColumnIndexOrThrow(CURRENCY_QUOTE_QUOTE_DATE);
                currencyQuote.quote_date = cursor.getString(quote_dateIndex);
            }
            if (cursor.getColumnIndex(CURRENCY_QUOTE_CURRENCY_VALUE) != -1) {
                currency_valueIndex = cursor.getColumnIndexOrThrow(CURRENCY_QUOTE_CURRENCY_VALUE);
                currencyQuote.currency_value = cursor.getInt(currency_valueIndex);
            }
        }
        return currencyQuote;
    }

    private void setContentValue(CurrencyQuote currencyQuote) {
        initialValues = new ContentValues();
        initialValues.put(CURRENCY_QUOTE_ID, currencyQuote.id);
        initialValues.put(CURRENCY_QUOTE_CURRENCY_TYPE, currencyQuote.currency_type);
        initialValues.put(CURRENCY_QUOTE_QUOTE_DATE, currencyQuote.quote_date);
        initialValues.put(CURRENCY_QUOTE_CURRENCY_VALUE, currencyQuote.currency_value);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

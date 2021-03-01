package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public CurrencyQuote fetchCurrencyQuoteById(Integer id) {
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

    public void deleteCurrencyQuote(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        super.delete(CURRENCY_QUOTE_TABLE, selection, selectionArgs);
    }

    public boolean updateCurrencyQuote(CurrencyQuote currencyQuote) {
        setContentValue(currencyQuote);
        final String[] selectionArgs = { String.valueOf(currencyQuote.getId()) };
        final String selection = CURRENCY_QUOTE_ID + " = ?";
        return (super.update(CURRENCY_QUOTE_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addCurrencyQuote(CurrencyQuote currencyQuote) {
        setContentValue(currencyQuote);
        return (super.insert(CURRENCY_QUOTE_TABLE, getContentValue()) > 0);
    }

    protected CurrencyQuote cursorToEntity(Cursor c) {
        CurrencyQuote cQ = new CurrencyQuote();
        if (c != null) {
            if (c.getColumnIndex(CURRENCY_QUOTE_ID) != -1)              {cQ.id = c.getInt(c.getColumnIndexOrThrow(CURRENCY_QUOTE_ID)); }
            if (c.getColumnIndex(CURRENCY_QUOTE_CURRENCY_TYPE) != -1)   {cQ.currency_type = c.getInt(c.getColumnIndexOrThrow(CURRENCY_QUOTE_CURRENCY_TYPE)); }
            if (c.getColumnIndex(CURRENCY_QUOTE_QUOTE_DATE) != -1)      {cQ.quote_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(CURRENCY_QUOTE_QUOTE_DATE))); }
            if (c.getColumnIndex(CURRENCY_QUOTE_CURRENCY_VALUE) != -1)  {cQ.currency_value = c.getDouble(c.getColumnIndexOrThrow(CURRENCY_QUOTE_CURRENCY_VALUE)); }
        }
        return cQ;
    }

    private void setContentValue(CurrencyQuote cQ) {
        initialValues = new ContentValues();
        initialValues.put(CURRENCY_QUOTE_ID, cQ.id);
        initialValues.put(CURRENCY_QUOTE_CURRENCY_TYPE, cQ.currency_type);
        initialValues.put(CURRENCY_QUOTE_QUOTE_DATE, Utils.dateFormat(cQ.quote_date));
        initialValues.put(CURRENCY_QUOTE_CURRENCY_VALUE, cQ.currency_value);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

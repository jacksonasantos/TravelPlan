package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.CurrencyQuoteISchema;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationISchema;
import com.jacksonasantos.travelplan.dao.interfaces.TransportISchema;
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

    public CurrencyQuote findDayQuote(int currency_type, Date quote_date) {

        CurrencyQuote currencyQuote = new CurrencyQuote();
        if (quote_date == null ) {
            return currencyQuote;
        }
        cursor = super.rawQuery("SELECT * " +
                                     " FROM " + CURRENCY_QUOTE_TABLE +
                                    " WHERE " + CURRENCY_QUOTE_CURRENCY_TYPE + "=? " +
                                      " AND " + CURRENCY_QUOTE_QUOTE_DATE + "=?",
                new String[] { String.valueOf(currency_type), Utils.dateToStringDate(quote_date)});
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                 cursor.moveToFirst();
                currencyQuote = cursorToEntity(cursor);
            }
        cursor.close();
        }
        return currencyQuote;
    }
/*
SELECT DISTINCT r.currency_type  currency_type
  FROM reservation r
 WHERE NOT EXISTS (SELECT 1
                     FROM currency_quote cq
                    WHERE cq.currency_type = r.currency_type
                      AND DATE(cq.quote_date ) = DATE('NOW'))
   AND (r.currency_type != null OR r.currency_type > 0)
   AND r.STATUS_RESERVATION != 3
UNION
SELECT DISTINCT t.currency_type  currency_type
  FROM transport t
 WHERE NOT EXISTS (SELECT 1
                     FROM currency_quote cq
                    WHERE cq.currency_type = t.currency_type
                      AND DATE(cq.quote_date ) = DATE('NOW'))
   AND (t.currency_type != null OR t.currency_type > 0)
   AND t.end_date >= date('now')
UNION
SELECT DISTINCT i.currency_type currency_type
  FROM insurance i
 WHERE NOT EXISTS (SELECT 1
                     FROM currency_quote cq
                    WHERE cq.currency_type = i.currency_type
                      AND DATE(cq.quote_date ) = DATE('NOW'))
   AND (i.currency_type != null OR i.currency_type > 0)
   AND i.status >= 1
 */
    public List<Integer> findCurrencyNoQuoteDay() {
        List<Integer> list = new ArrayList<>();
        cursor = super.rawQuery(
                " SELECT DISTINCT r." + ReservationISchema.RESERVATION_CURRENCY_TYPE + " currency_type " +
                      " FROM " + ReservationISchema.RESERVATION_TABLE + " r " +
                     " WHERE NOT EXISTS (SELECT 1 " +
                                         " FROM " + CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE + " cq " +
                                        " WHERE cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE + " = r." + ReservationISchema.RESERVATION_CURRENCY_TYPE +
                                          " AND DATE(cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_QUOTE_DATE + ") = DATE('NOW')) " +
                       " AND (r." + ReservationISchema.RESERVATION_CURRENCY_TYPE + " != null OR r." + ReservationISchema.RESERVATION_CURRENCY_TYPE+ " > 0) " +
                       " AND r." + ReservationISchema.RESERVATION_STATUS_RESERVATION + " != 3 "+
                     " UNION " +
                    " SELECT DISTINCT t."+ TransportISchema.TRANSPORT_CURRENCY_TYPE +" currency_type " +
                      " FROM " + TransportISchema.TRANSPORT_TABLE+ " t " +
                     " WHERE NOT EXISTS (SELECT 1 " +
                                         " FROM " + CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE + " cq " +
                                        " WHERE cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE + " = t." + TransportISchema.TRANSPORT_CURRENCY_TYPE +
                                          " AND DATE(cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_QUOTE_DATE + ") = DATE('NOW')) " +
                       " AND (t." + TransportISchema.TRANSPORT_CURRENCY_TYPE + " != null OR t." + TransportISchema.TRANSPORT_CURRENCY_TYPE + " > 0) " +
                       " AND t."+TransportISchema.TRANSPORT_END_DATE + " >= DATE('now')" +
                     " UNION " +
                    " SELECT DISTINCT i." + InsuranceISchema.INSURANCE_CURRENCY_TYPE + " currency_type "+
                      " FROM " + InsuranceISchema.INSURANCE_TABLE + " i " +
                     " WHERE NOT EXISTS (SELECT 1 " +
                                         " FROM " + CurrencyQuoteISchema.CURRENCY_QUOTE_TABLE + " cq " +
                                        " WHERE cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_CURRENCY_TYPE + " = i." + InsuranceISchema.INSURANCE_CURRENCY_TYPE +
                                          " AND DATE(cq." + CurrencyQuoteISchema.CURRENCY_QUOTE_QUOTE_DATE + ") = DATE('NOW')) " +
                       " AND (i." + CURRENCY_QUOTE_CURRENCY_TYPE + " != null OR i."+InsuranceISchema.INSURANCE_CURRENCY_TYPE + " > 0) " +
                       " AND i." + InsuranceISchema.INSURANCE_STATUS + " >= 1", null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getColumnIndex("currency_type") != -1) {
                    list.add(cursor.getInt(cursor.getColumnIndexOrThrow("currency_type")));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
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

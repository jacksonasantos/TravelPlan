package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.TransportIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.TransportISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TransportDAO extends DbContentProvider implements TransportISchema, TransportIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public TransportDAO(SQLiteDatabase db) {
        super(db);
    }

    public Transport fetchTransportById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRANSPORT_ID + " = ?";
        Transport transport = new Transport();
        cursor = super.query(TRANSPORT_TABLE, TRANSPORT_COLUMNS, selection, selectionArgs, TRANSPORT_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                transport = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return transport;
    }

    public List<Transport> fetchAllTransport() {
        List<Transport> transportList = new ArrayList<>();
        cursor = super.query(TRANSPORT_TABLE, TRANSPORT_COLUMNS, null, null, TRANSPORT_START_DATE );
        if (cursor.moveToFirst()) {
            do {
                Transport transport = cursorToEntity(cursor);
                transportList.add(transport);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return transportList;
    }

    public void deleteTransport(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = TRANSPORT_ID + " = ?";
        super.delete(TRANSPORT_TABLE, selection, selectionArgs);
    }

    public boolean updateTransport(Transport transport) {
        setContentValue(transport);
        final String[] selectionArgs = { String.valueOf(transport.getId()) };
        final String selection = TRANSPORT_ID + " = ?";
        return (super.update(TRANSPORT_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public Integer addTransport(Transport transport) {
        setContentValue(transport);
        return Math.toIntExact(super.insert(TRANSPORT_TABLE, getContentValue()));
    }

    protected Transport cursorToEntity(Cursor c) {
        Transport t = new Transport();
        if (c != null) {
            if (c.getColumnIndex(TRANSPORT_ID) != -1)              {t.setId(c.getInt(c.getColumnIndexOrThrow(TRANSPORT_ID))); }
            if (c.getColumnIndex(TRANSPORT_TRAVEL_ID) != -1)       {t.setTravel_id(c.getInt(c.getColumnIndexOrThrow(TRANSPORT_TRAVEL_ID))); }
            if (c.getColumnIndex(TRANSPORT_TRANSPORT_TYPE) != -1)  {t.setTransport_type(c.getInt(c.getColumnIndexOrThrow(TRANSPORT_TRANSPORT_TYPE))); }
            if (c.getColumnIndex(TRANSPORT_IDENTIFIER) != -1)      {t.setIdentifier(c.getString(c.getColumnIndexOrThrow(TRANSPORT_IDENTIFIER))); }
            if (c.getColumnIndex(TRANSPORT_DESCRIPTION) != -1)     {t.setDescription(c.getString(c.getColumnIndexOrThrow(TRANSPORT_DESCRIPTION))); }
            if (c.getColumnIndex(TRANSPORT_COMPANY) != -1)         {t.setCompany(c.getString(c.getColumnIndexOrThrow(TRANSPORT_COMPANY))); }
            if (c.getColumnIndex(TRANSPORT_CONTACT) != -1)         {t.setContact(c.getString(c.getColumnIndexOrThrow(TRANSPORT_CONTACT))); }
            if (c.getColumnIndex(TRANSPORT_START_LOCATION) != -1)  {t.setStart_location(c.getString(c.getColumnIndexOrThrow(TRANSPORT_START_LOCATION))); }
            if (c.getColumnIndex(TRANSPORT_START_DATE) != -1)      {t.setStart_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TRANSPORT_START_DATE)))); }
            if (c.getColumnIndex(TRANSPORT_END_LOCATION) != -1)    {t.setEnd_location(c.getString(c.getColumnIndexOrThrow(TRANSPORT_END_LOCATION))); }
            if (c.getColumnIndex(TRANSPORT_END_DATE) != -1)        {t.setEnd_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(TRANSPORT_END_DATE)))); }
            if (c.getColumnIndex(TRANSPORT_SERVICE_VALUE) != -1)   {t.setService_value(c.getDouble(c.getColumnIndexOrThrow(TRANSPORT_SERVICE_VALUE))); }
            if (c.getColumnIndex(TRANSPORT_SERVICE_TAX) != -1)     {t.setService_tax(c.getDouble(c.getColumnIndexOrThrow(TRANSPORT_SERVICE_TAX))); }
            if (c.getColumnIndex(TRANSPORT_AMOUNT_PAID) != -1)     {t.setAmount_paid(c.getDouble(c.getColumnIndexOrThrow(TRANSPORT_AMOUNT_PAID))); }
            if (c.getColumnIndex(TRANSPORT_NOTE) != -1)            {t.setNote(c.getString(c.getColumnIndexOrThrow(TRANSPORT_NOTE))); }
        }
        return t;
    }

    private void setContentValue(Transport t) {
        initialValues = new ContentValues();
        initialValues.put(TRANSPORT_ID, t.id);
        initialValues.put(TRANSPORT_TRAVEL_ID, t.travel_id);
        initialValues.put(TRANSPORT_TRANSPORT_TYPE, t.transport_type);
        initialValues.put(TRANSPORT_IDENTIFIER, t.identifier);
        initialValues.put(TRANSPORT_DESCRIPTION, t.description);
        initialValues.put(TRANSPORT_COMPANY, t.company);
        initialValues.put(TRANSPORT_CONTACT, t.contact);
        initialValues.put(TRANSPORT_START_LOCATION, t.start_location);
        initialValues.put(TRANSPORT_START_DATE, Utils.dateFormat(t.start_date));
        initialValues.put(TRANSPORT_END_LOCATION, t.end_location);
        initialValues.put(TRANSPORT_END_DATE, Utils.dateFormat(t.end_date));
        initialValues.put(TRANSPORT_SERVICE_VALUE, t.service_value);
        initialValues.put(TRANSPORT_SERVICE_TAX, t.service_tax);
        initialValues.put(TRANSPORT_AMOUNT_PAID, t.amount_paid);
        initialValues.put(TRANSPORT_NOTE, t.note);
    }
    private ContentValues getContentValue() {
        return initialValues;
    }
}
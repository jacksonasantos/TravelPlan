package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.BrokerIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.BrokerISchema;

import java.util.ArrayList;
import java.util.List;

public class BrokerDAO extends DbContentProvider implements BrokerISchema, BrokerIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public BrokerDAO(SQLiteDatabase db) {
        super(db);
    }

    public Broker fetchBrokerById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = BROKER_ID + " = ?";
        Broker broker = new Broker();
        cursor = super.query(BROKER_TABLE, BROKER_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                broker = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return broker;
    }

    public List<Broker> fetchAllBroker() {
        List<Broker> brokerList = new ArrayList<>();

        cursor = super.query(BROKER_TABLE, BROKER_COLUMNS, null,null, BROKER_NAME);

        if (cursor.moveToFirst()) {
            do {
                Broker broker = cursorToEntity(cursor);
                brokerList.add(broker);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return brokerList;
    }

    public ArrayList<Broker> fetchArrayBroker(){
        ArrayList<Broker> brokerList = new ArrayList<>();
        Cursor cursor = super.query(BROKER_TABLE, BROKER_COLUMNS, null,null, BROKER_NAME);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Broker broker = cursorToEntity(cursor);
                brokerList.add(broker);
            }while(cursor.moveToNext());
        }
        return brokerList;
    }

    public void deleteBroker(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = BROKER_ID + " = ?";
        super.delete(BROKER_TABLE, selection, selectionArgs);
    }

    public boolean updateBroker(Broker broker) {
        setContentValue(broker);
        final String[] selectionArgs = { String.valueOf(broker.getId()) };
        final String selection = BROKER_ID + " = ?";
        try {
            return (super.update(BROKER_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addBroker(Broker broker) {
        setContentValue(broker);
        try {
            return (super.insert(BROKER_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", ex.getMessage());
            return false;
        }
    }

    protected Broker cursorToEntity(Cursor c) {
        Broker b = new Broker();
        if (c != null) {
            if (c.getColumnIndex(BROKER_ID) != -1)           {b.id = c.getInt(c.getColumnIndexOrThrow(BROKER_ID)); }
            if (c.getColumnIndex(BROKER_NAME) != -1)         {b.name = c.getString(c.getColumnIndexOrThrow(BROKER_NAME)); }
            if (c.getColumnIndex(BROKER_PHONE) != -1)        {b.phone = c.getString(c.getColumnIndexOrThrow(BROKER_PHONE)); }
            if (c.getColumnIndex(BROKER_EMAIL) != -1)        {b.email = c.getString(c.getColumnIndexOrThrow(BROKER_EMAIL)); }
            if (c.getColumnIndex(BROKER_CONTACT_NAME) != -1) {b.contact_name = c.getString(c.getColumnIndexOrThrow(BROKER_CONTACT_NAME));
            }
        }
        return b;
    }

    private void setContentValue(Broker b) {
        initialValues = new ContentValues();
        initialValues.put(BROKER_ID, b.id);
        initialValues.put(BROKER_NAME, b.name);
        initialValues.put(BROKER_PHONE, b.phone);
        initialValues.put(BROKER_EMAIL, b.email);
        initialValues.put(BROKER_CONTACT_NAME, b.contact_name);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

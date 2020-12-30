package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    protected Broker cursorToEntity(Cursor cursor) {

        Broker broker = new Broker();

        int idIndex;
        int nameIndex;
        int phoneIndex;
        int emailIndex;
        int contact_nameIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(BROKER_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(BROKER_ID);
                broker.id = cursor.getInt(idIndex);
            }
            if (cursor.getColumnIndex(BROKER_NAME) != -1) {
                nameIndex = cursor.getColumnIndexOrThrow(BROKER_NAME);
                broker.name = cursor.getString(nameIndex);
            }
            if (cursor.getColumnIndex(BROKER_PHONE) != -1) {
                phoneIndex = cursor.getColumnIndexOrThrow(BROKER_PHONE);
                broker.phone = cursor.getString(phoneIndex);
            }
            if (cursor.getColumnIndex(BROKER_EMAIL) != -1) {
                emailIndex = cursor.getColumnIndexOrThrow(BROKER_EMAIL);
                broker.email = cursor.getString(emailIndex);
            }
            if (cursor.getColumnIndex(BROKER_CONTACT_NAME) != -1) {
                contact_nameIndex = cursor.getColumnIndexOrThrow(BROKER_CONTACT_NAME);
                broker.contact_name = cursor.getString(contact_nameIndex);
            }
        }
        return broker;
    }

    private void setContentValue(Broker broker) {
        initialValues = new ContentValues();
        initialValues.put(BROKER_ID, broker.id);
        initialValues.put(BROKER_NAME, broker.name);
        initialValues.put(BROKER_PHONE, broker.phone);
        initialValues.put(BROKER_EMAIL, broker.email);
        initialValues.put(BROKER_CONTACT_NAME, broker.contact_name);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

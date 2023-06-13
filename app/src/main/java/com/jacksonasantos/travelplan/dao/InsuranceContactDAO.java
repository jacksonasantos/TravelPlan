package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceContactIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceContactISchema;

import java.util.ArrayList;
import java.util.List;

public class InsuranceContactDAO extends DbContentProvider implements InsuranceContactISchema, InsuranceContactIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public InsuranceContactDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<InsuranceContact> fetchInsuranceContactByInsurance(Integer insurance_id) {
        final String[] selectionArgs = { String.valueOf(insurance_id) };
        final String selection = INSURANCE_CONTACT_INSURANCE_ID + " = ?";
        List<InsuranceContact> insuranceContactList = new ArrayList<>();
        cursor = super.query(INSURANCE_CONTACT_TABLE, INSURANCE_CONTACT_COLUMNS, selection, selectionArgs, INSURANCE_CONTACT_TYPE_CONTACT);
        if (cursor.moveToFirst()) {
            do {
                InsuranceContact insuranceContact = cursorToEntity(cursor);
                insuranceContactList.add(insuranceContact);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return insuranceContactList;
    }

    public void deleteInsuranceContact(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = INSURANCE_CONTACT_ID + " = ?";
        super.delete(INSURANCE_CONTACT_TABLE, selection, selectionArgs);
    }

    public boolean addInsuranceContact(InsuranceContact insuranceContact) {
        setContentValue(insuranceContact);
        return (super.insert(INSURANCE_CONTACT_TABLE, getContentValue()) > 0);
    }

    protected InsuranceContact cursorToEntity(Cursor c) {
        InsuranceContact iC = new InsuranceContact();
        if (c != null) {
            if (c.getColumnIndex(INSURANCE_CONTACT_ID) != -1)                  {iC.setId(c.getInt(c.getColumnIndexOrThrow(INSURANCE_CONTACT_ID)));}
            if (c.getColumnIndex(INSURANCE_CONTACT_INSURANCE_ID) != -1)        if (c.getInt(c.getColumnIndexOrThrow(INSURANCE_CONTACT_INSURANCE_ID)) == 0) iC.setInsurance_id(null);
                                                                               else iC.setInsurance_id(c.getInt(c.getColumnIndexOrThrow(INSURANCE_CONTACT_INSURANCE_ID)));
            if (c.getColumnIndex(INSURANCE_CONTACT_TYPE_CONTACT) != -1)        {iC.setType_contact(c.getString(c.getColumnIndexOrThrow(INSURANCE_CONTACT_TYPE_CONTACT))); }
            if (c.getColumnIndex(INSURANCE_CONTACT_DESCRIPTION_CONTACT) != -1) {iC.setDescription_contact(c.getString(c.getColumnIndexOrThrow(INSURANCE_CONTACT_DESCRIPTION_CONTACT)));}
            if (c.getColumnIndex(INSURANCE_CONTACT_DETAIL_CONTACT) != -1)      {iC.setDetail_contact(c.getString(c.getColumnIndexOrThrow(INSURANCE_CONTACT_DETAIL_CONTACT))); }
        }
        return iC;
    }

    private void setContentValue(InsuranceContact iC) {
        initialValues = new ContentValues();
        initialValues.put(INSURANCE_CONTACT_ID, iC.id);
        initialValues.put(INSURANCE_CONTACT_INSURANCE_ID, iC.insurance_id);
        initialValues.put(INSURANCE_CONTACT_TYPE_CONTACT, iC.type_contact);
        initialValues.put(INSURANCE_CONTACT_DESCRIPTION_CONTACT, iC.description_contact);
        initialValues.put(INSURANCE_CONTACT_DETAIL_CONTACT, iC.detail_contact);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}

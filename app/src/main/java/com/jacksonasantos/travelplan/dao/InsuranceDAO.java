package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class InsuranceDAO extends DbContentProvider implements InsuranceISchema, InsuranceIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public InsuranceDAO(SQLiteDatabase db) {
        super(db);
    }

    public Insurance fetchInsuranceById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = INSURANCE_ID + " = ?";
        Insurance insurance = new Insurance();
        cursor = super.query(INSURANCE_TABLE, INSURANCE_COLUMNS, selection, selectionArgs, INSURANCE_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                insurance = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return insurance;
    }

    public Insurance fetchInsuranceByPolicy(String insurance_policy) {
        final String[] selectionArgs = { insurance_policy };
        final String selection = INSURANCE_INSURANCE_POLICY + " = ?";
        Insurance insurance = new Insurance();
        cursor = super.query(INSURANCE_TABLE, INSURANCE_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                insurance = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return insurance;
    }

    public List<Insurance> findReminderInsurance( String type,  Integer id) {
        List<Insurance> insuranceList = new ArrayList<>();

        final String[] selectionArgs = { String.valueOf(id) };
        final String selection;
        if (type.equals("V")) {
            selection = INSURANCE_VEHICLE_ID + " = ? AND ";
        } else {
            selection = INSURANCE_TRAVEL_ID + " = ? AND ";
        }
        final String selectionField = selection + INSURANCE_STATUS + " = 0 ";

        cursor = super.query(INSURANCE_TABLE, INSURANCE_COLUMNS, selectionField, selectionArgs, INSURANCE_FINAL_EFFECTIVE_DATE);
        if (cursor.moveToFirst()) {
            do {
                Insurance insurance = cursorToEntity(cursor);
                insuranceList.add(insurance);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return insuranceList;
    }

    public List<Insurance> fetchAllInsurance() {
        List<Insurance> insuranceList = new ArrayList<>();
        cursor = super.query(INSURANCE_TABLE, INSURANCE_COLUMNS, null, null, INSURANCE_INSURANCE_POLICY);
        if (cursor.moveToFirst()) {
            do {
                Insurance insurance = cursorToEntity(cursor);
                insuranceList.add(insurance);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return insuranceList;
    }

    public void deleteInsurance(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = INSURANCE_ID + " = ?";
        super.delete(INSURANCE_TABLE, selection, selectionArgs);
    }

    public boolean updateInsurance(Insurance insurance) {
        setContentValue(insurance);
        final String[] selectionArgs = { String.valueOf(insurance.getId()) };
        final String selection = INSURANCE_ID + " = ?";
        return (super.update(INSURANCE_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addInsurance(Insurance insurance) {
        setContentValue(insurance);
        return (super.insert(INSURANCE_TABLE, getContentValue()) > 0);
    }

    protected Insurance cursorToEntity(Cursor c) {
        Insurance i = new Insurance();
        if (c != null) {
            if (c.getColumnIndex(INSURANCE_ID) != -1)                      {i.setId(c.getInt(c.getColumnIndexOrThrow(INSURANCE_ID))); }
            if (c.getColumnIndex(INSURANCE_INSURANCE_COMPANY_ID) != -1)    {i.setInsurance_company_id(c.getInt(c.getColumnIndexOrThrow(INSURANCE_INSURANCE_COMPANY_ID))); }
            if (c.getColumnIndex(INSURANCE_BROKER_ID) != -1)               {i.setBroker_id(c.getInt(c.getColumnIndexOrThrow(INSURANCE_BROKER_ID))); }
            if (c.getColumnIndex(INSURANCE_INSURANCE_TYPE) != -1)          {i.setInsurance_type(c.getInt(c.getColumnIndexOrThrow(INSURANCE_INSURANCE_TYPE))); }
            if (c.getColumnIndex(INSURANCE_DESCRIPTION) != -1)             {i.setDescription(c.getString(c.getColumnIndexOrThrow(INSURANCE_DESCRIPTION))); }
            if (c.getColumnIndex(INSURANCE_INSURANCE_POLICY) != -1)        {i.setInsurance_policy(c.getString(c.getColumnIndexOrThrow(INSURANCE_INSURANCE_POLICY))); }
            if (c.getColumnIndex(INSURANCE_ISSUANCE_DATE) != -1)           {i.setIssuance_date(Utils.dateParse(c.getString( c.getColumnIndexOrThrow(INSURANCE_ISSUANCE_DATE)))); }
            if (c.getColumnIndex(INSURANCE_INITIAL_EFFECTIVE_DATE) != -1)  {i.setInitial_effective_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(INSURANCE_INITIAL_EFFECTIVE_DATE)))); }
            if (c.getColumnIndex(INSURANCE_FINAL_EFFECTIVE_DATE) != -1)    {i.setFinal_effective_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(INSURANCE_FINAL_EFFECTIVE_DATE)))); }
            if (c.getColumnIndex(INSURANCE_NET_PREMIUM_VALUE) != -1)       {i.setNet_premium_value(c.getDouble(c.getColumnIndexOrThrow(INSURANCE_NET_PREMIUM_VALUE))); }
            if (c.getColumnIndex(INSURANCE_TAX_AMOUNT) != -1)              {i.setTax_amount(c.getDouble(c.getColumnIndexOrThrow(INSURANCE_TAX_AMOUNT))); }
            if (c.getColumnIndex(INSURANCE_TOTAL_PREMIUM_VALUE) != -1)     {i.setTotal_premium_value(c.getDouble(c.getColumnIndexOrThrow(INSURANCE_TOTAL_PREMIUM_VALUE))); }
            if (c.getColumnIndex(INSURANCE_INSURANCE_DEDUCTIBLE) != -1)    {i.setInsurance_deductible(c.getDouble( c.getColumnIndexOrThrow(INSURANCE_INSURANCE_DEDUCTIBLE))); }
            if (c.getColumnIndex(INSURANCE_BONUS_CLASS) != -1)             {i.setBonus_class(c.getInt(c.getColumnIndexOrThrow(INSURANCE_BONUS_CLASS))); }
            if (c.getColumnIndex(INSURANCE_NOTE) != -1)                    {i.setNote(c.getString(c.getColumnIndexOrThrow(INSURANCE_NOTE))); }
            if (c.getColumnIndex(INSURANCE_STATUS) != -1)                  {i.setStatus(c.getInt(c.getColumnIndexOrThrow(INSURANCE_STATUS))); }
            if (c.getColumnIndex(INSURANCE_TRAVEL_ID) != -1)               {i.setTravel_id(c.getInt(c.getColumnIndexOrThrow(INSURANCE_TRAVEL_ID))); }
            if (c.getColumnIndex(INSURANCE_VEHICLE_ID) != -1)              {i.setVehicle_id(c.getInt( c.getColumnIndexOrThrow(INSURANCE_VEHICLE_ID))); }
        }
        return i;
    }

    private void setContentValue(Insurance i) {
        initialValues = new ContentValues();
        initialValues.put(INSURANCE_ID, i.id);
        initialValues.put(INSURANCE_INSURANCE_COMPANY_ID, i.insurance_company_id);
        initialValues.put(INSURANCE_BROKER_ID, i.broker_id);
        initialValues.put(INSURANCE_INSURANCE_TYPE, i.insurance_type);
        initialValues.put(INSURANCE_INSURANCE_POLICY, i.insurance_policy);
        initialValues.put(INSURANCE_DESCRIPTION, i.description);
        initialValues.put(INSURANCE_ISSUANCE_DATE, Utils.dateFormat(i.issuance_date));
        initialValues.put(INSURANCE_INITIAL_EFFECTIVE_DATE, Utils.dateFormat(i.initial_effective_date));
        initialValues.put(INSURANCE_FINAL_EFFECTIVE_DATE, Utils.dateFormat(i.final_effective_date));
        initialValues.put(INSURANCE_NET_PREMIUM_VALUE, i.net_premium_value);
        initialValues.put(INSURANCE_TAX_AMOUNT, i.tax_amount);
        initialValues.put(INSURANCE_TOTAL_PREMIUM_VALUE, i.total_premium_value);
        initialValues.put(INSURANCE_INSURANCE_DEDUCTIBLE, i.insurance_deductible);
        initialValues.put(INSURANCE_BONUS_CLASS, i.bonus_class);
        initialValues.put(INSURANCE_NOTE, i.note);
        initialValues.put(INSURANCE_STATUS, i.status);
        initialValues.put(INSURANCE_TRAVEL_ID, i.travel_id);
        initialValues.put(INSURANCE_VEHICLE_ID, i.vehicle_id);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

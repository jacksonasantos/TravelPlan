package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public List<Insurance> findReminderInsurance() {
        List<Insurance> insuranceList = new ArrayList<>();

        final String[] selectionArgs = { };
        final String selection = INSURANCE_STATUS + " = 0 "; //AND ( " +
        //            MAINTENANCE_EXPIRATION_DATE + " NOT NULL AND " +
        //            MAINTENANCE_EXPIRATION_DATE + " > DATE ('now') )" ;

        cursor = super.query(INSURANCE_TABLE, INSURANCE_COLUMNS, selection, selectionArgs, INSURANCE_FINAL_EFFECTIVE_DATE);

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

    protected Insurance cursorToEntity(Cursor cursor) {

        Insurance insurance = new Insurance();

        int idIndex;
        int insurance_company_idIndex;
        int broker_idIndex;
        int insurance_typeIndex;
        int descriptionIndex;
        int insurance_policyIndex;
        int issuance_dateIndex;
        int initial_effective_dateIndex;
        int final_effective_dateIndex;
        int net_premium_valueIndex;
        int tax_amountIndex;
        int total_premium_valueIndex;
        int insurance_deductibleIndex;
        int bonus_classIndex;
        int noteIndex;
        int statusIndex;
        int travel_idIndex;
        int vehicle_idIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(INSURANCE_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(INSURANCE_ID);
                insurance.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_INSURANCE_COMPANY_ID) != -1) {
                insurance_company_idIndex = cursor.getColumnIndexOrThrow(INSURANCE_INSURANCE_COMPANY_ID);
                insurance.setInsurance_company_id(cursor.getInt(insurance_company_idIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_BROKER_ID) != -1) {
                broker_idIndex = cursor.getColumnIndexOrThrow(INSURANCE_BROKER_ID);
                insurance.setBroker_id(cursor.getInt(broker_idIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_INSURANCE_TYPE) != -1) {
                insurance_typeIndex = cursor.getColumnIndexOrThrow(INSURANCE_INSURANCE_TYPE);
                insurance.setInsurance_type(cursor.getInt(insurance_typeIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_DESCRIPTION) != -1) {
                descriptionIndex = cursor.getColumnIndexOrThrow(INSURANCE_DESCRIPTION);
                insurance.setDescription(cursor.getString(descriptionIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_INSURANCE_POLICY) != -1) {
                insurance_policyIndex = cursor.getColumnIndexOrThrow(INSURANCE_INSURANCE_POLICY);
                insurance.setInsurance_policy(cursor.getString(insurance_policyIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_ISSUANCE_DATE) != -1) {
                issuance_dateIndex = cursor.getColumnIndexOrThrow(INSURANCE_ISSUANCE_DATE);
                insurance.setIssuance_date(Utils.dateParse(cursor.getString(issuance_dateIndex)));
            }
            if (cursor.getColumnIndex(INSURANCE_INITIAL_EFFECTIVE_DATE) != -1) {
                initial_effective_dateIndex = cursor.getColumnIndexOrThrow(INSURANCE_INITIAL_EFFECTIVE_DATE);
                insurance.setInitial_effective_date(Utils.dateParse(cursor.getString(initial_effective_dateIndex)));
            }
            if (cursor.getColumnIndex(INSURANCE_FINAL_EFFECTIVE_DATE) != -1) {
                final_effective_dateIndex = cursor.getColumnIndexOrThrow(INSURANCE_FINAL_EFFECTIVE_DATE);
                insurance.setFinal_effective_date(Utils.dateParse(cursor.getString(final_effective_dateIndex)));
            }
            if (cursor.getColumnIndex(INSURANCE_NET_PREMIUM_VALUE) != -1) {
                net_premium_valueIndex = cursor.getColumnIndexOrThrow(INSURANCE_NET_PREMIUM_VALUE);
                insurance.setNet_premium_value(cursor.getDouble(net_premium_valueIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_TAX_AMOUNT) != -1) {
                tax_amountIndex = cursor.getColumnIndexOrThrow(INSURANCE_TAX_AMOUNT);
                insurance.setTax_amount(cursor.getDouble(tax_amountIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_TOTAL_PREMIUM_VALUE) != -1) {
                total_premium_valueIndex = cursor.getColumnIndexOrThrow(INSURANCE_TOTAL_PREMIUM_VALUE);
                insurance.setTotal_premium_value(cursor.getDouble(total_premium_valueIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_INSURANCE_DEDUCTIBLE) != -1) {
                insurance_deductibleIndex = cursor.getColumnIndexOrThrow(INSURANCE_INSURANCE_DEDUCTIBLE);
                insurance.setInsurance_deductible(cursor.getDouble(insurance_deductibleIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_BONUS_CLASS) != -1) {
                bonus_classIndex = cursor.getColumnIndexOrThrow(INSURANCE_BONUS_CLASS);
                insurance.setBonus_class(cursor.getInt(bonus_classIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_NOTE) != -1) {
                noteIndex = cursor.getColumnIndexOrThrow(INSURANCE_NOTE);
                insurance.setNote(cursor.getString(noteIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_STATUS) != -1) {
                statusIndex = cursor.getColumnIndexOrThrow(INSURANCE_STATUS);
                insurance.setStatus(cursor.getInt(statusIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_TRAVEL_ID) != -1) {
                travel_idIndex = cursor.getColumnIndexOrThrow(INSURANCE_TRAVEL_ID);
                insurance.setTravel_id(cursor.getInt(travel_idIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_VEHICLE_ID) != -1) {
                vehicle_idIndex = cursor.getColumnIndexOrThrow(INSURANCE_VEHICLE_ID);
                insurance.setVehicle_id(cursor.getInt(vehicle_idIndex));
            }
        }
        return insurance;
    }

    private void setContentValue(Insurance insurance) {
        initialValues = new ContentValues();
        initialValues.put(INSURANCE_ID, insurance.id);
        initialValues.put(INSURANCE_INSURANCE_COMPANY_ID, insurance.insurance_company_id);
        initialValues.put(INSURANCE_BROKER_ID, insurance.broker_id);
        initialValues.put(INSURANCE_INSURANCE_TYPE, insurance.insurance_type);
        initialValues.put(INSURANCE_INSURANCE_POLICY, insurance.insurance_policy);
        initialValues.put(INSURANCE_DESCRIPTION, insurance.description);
        initialValues.put(INSURANCE_ISSUANCE_DATE, Utils.dateFormat(insurance.issuance_date));
        initialValues.put(INSURANCE_INITIAL_EFFECTIVE_DATE, Utils.dateFormat(insurance.initial_effective_date));
        initialValues.put(INSURANCE_FINAL_EFFECTIVE_DATE, Utils.dateFormat(insurance.final_effective_date));
        initialValues.put(INSURANCE_NET_PREMIUM_VALUE, insurance.net_premium_value);
        initialValues.put(INSURANCE_TAX_AMOUNT, insurance.tax_amount);
        initialValues.put(INSURANCE_TOTAL_PREMIUM_VALUE, insurance.total_premium_value);
        initialValues.put(INSURANCE_INSURANCE_DEDUCTIBLE, insurance.insurance_deductible);
        initialValues.put(INSURANCE_BONUS_CLASS, insurance.bonus_class);
        initialValues.put(INSURANCE_NOTE, insurance.note);
        initialValues.put(INSURANCE_STATUS, insurance.status);
        initialValues.put(INSURANCE_TRAVEL_ID, insurance.travel_id);
        initialValues.put(INSURANCE_VEHICLE_ID, insurance.vehicle_id);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

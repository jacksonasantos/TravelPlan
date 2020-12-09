package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.InsuranceCompanyISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class InsuranceCompanyDAO extends DbContentProvider implements InsuranceCompanyISchema, InsuranceCompanyIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public InsuranceCompanyDAO(SQLiteDatabase db) {
        super(db);
    }

    public InsuranceCompany fetchInsuranceCompanyById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = INSURANCE_COMPANY_ID + " = ?";
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        cursor = super.query(INSURANCE_COMPANY_TABLE, INSURANCE_COMPANY_COLUMNS, selection, selectionArgs, INSURANCE_COMPANY_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                insuranceCompany = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return insuranceCompany;
    }

    public InsuranceCompany fetchInsuranceCompanyByCNPJ(String cnpj) {
        final String[] selectionArgs = { String.valueOf(cnpj) };
        final String selection = INSURANCE_COMPANY_CNPJ + " = ?";
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        cursor = super.query(INSURANCE_COMPANY_TABLE, INSURANCE_COMPANY_COLUMNS, selection, selectionArgs, INSURANCE_COMPANY_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                insuranceCompany = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return insuranceCompany;
    }

    public Integer fetchInsuranceCompanyByName(String name) {
        final String[] selectionArgs = { String.valueOf(name) };
        final String selection = INSURANCE_COMPANY_COMPANY_NAME + " = ?";
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        cursor = super.query(INSURANCE_COMPANY_TABLE, INSURANCE_COMPANY_COLUMNS, selection, selectionArgs, INSURANCE_COMPANY_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                insuranceCompany = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return insuranceCompany.getId();
    }

    public List<InsuranceCompany> fetchAllInsuranceCompanies() {
        List<InsuranceCompany> insuranceCompanyList = new ArrayList<>();
        cursor = super.query(INSURANCE_COMPANY_TABLE, INSURANCE_COMPANY_COLUMNS, null, null, INSURANCE_COMPANY_COMPANY_NAME);
        if (cursor.moveToFirst()) {
            do {
                InsuranceCompany insuranceCompany = cursorToEntity(cursor);
                insuranceCompanyList.add(insuranceCompany);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return insuranceCompanyList;
    }

    public ArrayList<InsuranceCompany> fetchArrayInsuranceCompany(){
        ArrayList<InsuranceCompany> insuranceCompanyList = new ArrayList<>();
        Cursor cursor = super.query(INSURANCE_COMPANY_TABLE, INSURANCE_COMPANY_COLUMNS, null,null, INSURANCE_COMPANY_COMPANY_NAME);
               if(cursor != null && cursor.moveToFirst()){
            do{
                InsuranceCompany insuranceCompany = cursorToEntity(cursor);
                insuranceCompanyList.add(insuranceCompany);
            }while(cursor.moveToNext());
        }
        return insuranceCompanyList;
    }

    public void deleteInsuranceCompany(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = INSURANCE_COMPANY_ID + " = ?";
        super.delete(INSURANCE_COMPANY_TABLE, selection, selectionArgs);
    }

    public boolean updateInsuranceCompany(InsuranceCompany insuranceCompany) {
        setContentValue(insuranceCompany);
        final String[] selectionArgs = { String.valueOf(insuranceCompany.getId()) };
        final String selection = INSURANCE_COMPANY_ID + " = ?";
        try {
            return (super.update(INSURANCE_COMPANY_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", ex.getMessage());
            return false;
        }
    }

    public boolean addInsuranceCompany(InsuranceCompany insuranceCompany) {
        setContentValue(insuranceCompany);
        try {
            return (super.insert(INSURANCE_COMPANY_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", ex.getMessage());
            return false;
        }
    }

    protected InsuranceCompany cursorToEntity(Cursor cursor) {

        InsuranceCompany insuranceCompany = new InsuranceCompany();

        int idIndex;
        int company_nameIndex;
        int cnpjIndex;
        int fip_codeIndex;
        int addressIndex;
        int cityIndex;
        int stateIndex;
        int zip_codeIndex;
        int telephoneIndex;
        int authorization_dateIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(INSURANCE_COMPANY_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_ID);
                insuranceCompany.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_COMPANY_NAME) != -1) {
                company_nameIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_COMPANY_NAME);
                insuranceCompany.setCompany_name(cursor.getString(company_nameIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_CNPJ) != -1) {
                cnpjIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_CNPJ);
                insuranceCompany.setCnpj(cursor.getString(cnpjIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_FIP_CODE) != -1) {
                fip_codeIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_FIP_CODE);
                insuranceCompany.setFip_code(cursor.getString(fip_codeIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_ADDRESS) != -1) {
                addressIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_ADDRESS);
                insuranceCompany.setAddress(cursor.getString(addressIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_CITY) != -1) {
                cityIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_CITY);
                insuranceCompany.setCity(cursor.getString(cityIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_STATE) != -1) {
                stateIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_STATE);
                insuranceCompany.setState(cursor.getString(stateIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_ZIP_CODE) != -1) {
                zip_codeIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_ZIP_CODE);
                insuranceCompany.setZip_code(cursor.getString(zip_codeIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_TELEPHONE) != -1) {
                telephoneIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_TELEPHONE);
                insuranceCompany.setTelephone(cursor.getString(telephoneIndex));
            }
            if (cursor.getColumnIndex(INSURANCE_COMPANY_AUTHORIZATION_DATE) != -1) {
                authorization_dateIndex = cursor.getColumnIndexOrThrow(INSURANCE_COMPANY_AUTHORIZATION_DATE);
                insuranceCompany.setAuthorization_date(Utils.stringToDate(cursor.getString(authorization_dateIndex)));
            }
        }
        return insuranceCompany;
    }

    private void setContentValue(InsuranceCompany insuranceCompany) {
        initialValues = new ContentValues();
        initialValues.put(INSURANCE_COMPANY_ID, insuranceCompany.id);
        initialValues.put(INSURANCE_COMPANY_COMPANY_NAME, insuranceCompany.company_name);
        initialValues.put(INSURANCE_COMPANY_CNPJ, insuranceCompany.cnpj);
        initialValues.put(INSURANCE_COMPANY_FIP_CODE, insuranceCompany.fip_code);
        initialValues.put(INSURANCE_COMPANY_ADDRESS, insuranceCompany.address);
        initialValues.put(INSURANCE_COMPANY_CITY, insuranceCompany.city);
        initialValues.put(INSURANCE_COMPANY_STATE, insuranceCompany.state);
        initialValues.put(INSURANCE_COMPANY_ZIP_CODE, insuranceCompany.zip_code);
        initialValues.put(INSURANCE_COMPANY_TELEPHONE, insuranceCompany.telephone);
        initialValues.put(INSURANCE_COMPANY_AUTHORIZATION_DATE, Utils.dateToString(insuranceCompany.authorization_date));
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

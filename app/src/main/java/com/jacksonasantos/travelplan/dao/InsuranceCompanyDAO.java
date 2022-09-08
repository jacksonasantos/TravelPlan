package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
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
        return (super.update(INSURANCE_COMPANY_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addInsuranceCompany(InsuranceCompany insuranceCompany) {
        setContentValue(insuranceCompany);
        return (super.insert(INSURANCE_COMPANY_TABLE, getContentValue()) > 0);
    }

    protected InsuranceCompany cursorToEntity(Cursor c) {
        InsuranceCompany iC = new InsuranceCompany();
        if (c != null) {
            if (c.getColumnIndex(INSURANCE_COMPANY_ID) != -1)                  {iC.setId(c.getInt(c.getColumnIndexOrThrow(INSURANCE_COMPANY_ID)));}
            if (c.getColumnIndex(INSURANCE_COMPANY_COMPANY_NAME) != -1)        {iC.setCompany_name(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_COMPANY_NAME))); }
            if (c.getColumnIndex(INSURANCE_COMPANY_CNPJ) != -1)                {iC.setCnpj(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_CNPJ))); }
            if (c.getColumnIndex(INSURANCE_COMPANY_FIP_CODE) != -1)            {iC.setFip_code(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_FIP_CODE)));}
            if (c.getColumnIndex(INSURANCE_COMPANY_ADDRESS) != -1)             {iC.setAddress(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_ADDRESS))); }
            if (c.getColumnIndex(INSURANCE_COMPANY_CITY) != -1)                {iC.setCity(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_CITY))); }
            if (c.getColumnIndex(INSURANCE_COMPANY_STATE) != -1)               {iC.setState(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_STATE)));}
            if (c.getColumnIndex(INSURANCE_COMPANY_ZIP_CODE) != -1)            {iC.setZip_code(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_ZIP_CODE))); }
            if (c.getColumnIndex(INSURANCE_COMPANY_TELEPHONE) != -1)           {iC.setTelephone(c.getString( c.getColumnIndexOrThrow(INSURANCE_COMPANY_TELEPHONE))); }
            if (c.getColumnIndex(INSURANCE_COMPANY_AUTHORIZATION_DATE) != -1)  {iC.setAuthorization_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(INSURANCE_COMPANY_AUTHORIZATION_DATE)))); }
        }
        return iC;
    }

    private void setContentValue(InsuranceCompany iC) {
        initialValues = new ContentValues();
        initialValues.put(INSURANCE_COMPANY_ID, iC.id);
        initialValues.put(INSURANCE_COMPANY_COMPANY_NAME, iC.company_name);
        initialValues.put(INSURANCE_COMPANY_CNPJ, iC.cnpj);
        initialValues.put(INSURANCE_COMPANY_FIP_CODE, iC.fip_code);
        initialValues.put(INSURANCE_COMPANY_ADDRESS, iC.address);
        initialValues.put(INSURANCE_COMPANY_CITY, iC.city);
        initialValues.put(INSURANCE_COMPANY_STATE, iC.state);
        initialValues.put(INSURANCE_COMPANY_ZIP_CODE, iC.zip_code);
        initialValues.put(INSURANCE_COMPANY_TELEPHONE, iC.telephone);
        initialValues.put(INSURANCE_COMPANY_AUTHORIZATION_DATE, Utils.dateFormat(iC.authorization_date));
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

}

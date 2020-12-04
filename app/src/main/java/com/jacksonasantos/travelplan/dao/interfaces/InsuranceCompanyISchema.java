package com.jacksonasantos.travelplan.dao.interfaces;

public interface InsuranceCompanyISchema {

    String INSURANCE_COMPANY_TABLE = "insurance_company";

    String INSURANCE_COMPANY_ID = "id";
    String INSURANCE_COMPANY_COMPANY_NAME = "company_name";
    String INSURANCE_COMPANY_CNPJ = "cnpj";
    String INSURANCE_COMPANY_FIP_CODE = "fip_code";
    String INSURANCE_COMPANY_ADDRESS = "address";
    String INSURANCE_COMPANY_CITY = "city";
    String INSURANCE_COMPANY_STATE = "state";
    String INSURANCE_COMPANY_ZIP_CODE = "zip_code";
    String INSURANCE_COMPANY_TELEPHONE = "telephone";
    String INSURANCE_COMPANY_AUTHORIZATION_DATE = "authorization_date";

    // Version 12
    String CREATE_TABLE_INSURANCE_COMPANY_V12 = "CREATE TABLE IF NOT EXISTS "
            + INSURANCE_COMPANY_TABLE + " ("
            + INSURANCE_COMPANY_ID + " INTEGER PRIMARY KEY, "
            + INSURANCE_COMPANY_COMPANY_NAME + " TEXT, "
            + INSURANCE_COMPANY_CNPJ + " TEXT, "
            + INSURANCE_COMPANY_FIP_CODE + " TEXT, "
            + INSURANCE_COMPANY_ADDRESS + " TEXT, "
            + INSURANCE_COMPANY_CITY + " TEXT, "
            + INSURANCE_COMPANY_STATE + " TEXT, "
            + INSURANCE_COMPANY_ZIP_CODE + " TEXT, "
            + INSURANCE_COMPANY_TELEPHONE + " TEXT, "
            + INSURANCE_COMPANY_AUTHORIZATION_DATE + " DATE "
            + ")";

    String[] INSURANCE_COMPANY_COLUMNS = new String[] {
            INSURANCE_COMPANY_ID,
            INSURANCE_COMPANY_COMPANY_NAME,
            INSURANCE_COMPANY_CNPJ,
            INSURANCE_COMPANY_FIP_CODE,
            INSURANCE_COMPANY_ADDRESS,
            INSURANCE_COMPANY_CITY,
            INSURANCE_COMPANY_STATE,
            INSURANCE_COMPANY_ZIP_CODE,
            INSURANCE_COMPANY_TELEPHONE,
            INSURANCE_COMPANY_AUTHORIZATION_DATE
   };
}

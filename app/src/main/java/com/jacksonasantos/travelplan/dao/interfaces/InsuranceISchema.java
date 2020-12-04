package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Broker;

public interface InsuranceISchema {

    String INSURANCE_TABLE = "insurance";

    String INSURANCE_ID = "id";
    String INSURANCE_INSURANCE_COMPANY_ID = "insurance_company_id";
    String INSURANCE_BROKER_ID = "broker_id";
    String INSURANCE_INSURANCE_TYPE = "insurance_type";
    String INSURANCE_DESCRIPTION = "description";
    String INSURANCE_INSURANCE_POLICY = "insurance_policy";
    String INSURANCE_ISSUANCE_DATE = "issuance_date";
    String INSURANCE_INITIAL_EFFECTIVE_DATE = "initial_effective_date";
    String INSURANCE_FINAL_EFFECTIVE_DATE = "final_effective_date";
    String INSURANCE_NET_PREMIUM_VALUE = "net_premium_value";
    String INSURANCE_TAX_AMOUNT = "tax_amount";
    String INSURANCE_TOTAL_PREMIUM_VALUE = "total_premium_value";
    String INSURANCE_INSURANCE_DEDUCTIBLE = "insurance_deductible";
    String INSURANCE_BONUS_CLASS = "bonus_class";
    String INSURANCE_NOTE = "note";
    String INSURANCE_STATUS = "status";
    String INSURANCE_TRAVEL_ID = "travel_id";
    String INSURANCE_VEHICLE_ID = "vehicle_id";

    // Version 16
    String CREATE_TABLE_INSURANCE_V16 = "CREATE TABLE IF NOT EXISTS "
            + INSURANCE_TABLE + " ("
            + INSURANCE_ID + " INTEGER PRIMARY KEY, "
            + INSURANCE_INSURANCE_COMPANY_ID + " LONG REFERENCES " + InsuranceCompanyISchema.INSURANCE_COMPANY_TABLE + ", "
            + INSURANCE_BROKER_ID + " LONG REFERENCES " + BrokerISchema.BROKER_TABLE + ", "
            + INSURANCE_INSURANCE_TYPE + " INT, "
            + INSURANCE_INSURANCE_POLICY + " TEXT, "
            + INSURANCE_ISSUANCE_DATE + " DATE, "
            + INSURANCE_INITIAL_EFFECTIVE_DATE + " DATE, "
            + INSURANCE_FINAL_EFFECTIVE_DATE + " DATE, "
            + INSURANCE_NET_PREMIUM_VALUE  + " DOUBLE, "
            + INSURANCE_TAX_AMOUNT + " DOUBLE, "
            + INSURANCE_TOTAL_PREMIUM_VALUE + " DOUBLE, "
            + INSURANCE_INSURANCE_DEDUCTIBLE + " DOUBLE, "
            + INSURANCE_BONUS_CLASS + " INT, "
            + INSURANCE_NOTE + " TEXT, "
            + INSURANCE_STATUS + " INT "
           + ")";

    // Version 18
    String ALTER_TABLE_INSURANCE_V18 = "ALTER TABLE " + INSURANCE_TABLE
            + " ADD COLUMN " + INSURANCE_DESCRIPTION + " STRING ";

    // Version 19
    String ALTER_TABLE_INSURANCE_V19_1 = "ALTER TABLE " + INSURANCE_TABLE
            + " ADD COLUMN " + INSURANCE_TRAVEL_ID + " LONG REFERENCES " + TravelISchema.TRAVEL_TABLE;
    String ALTER_TABLE_INSURANCE_V19_2 = "ALTER TABLE " + INSURANCE_TABLE
            + " ADD COLUMN " + INSURANCE_VEHICLE_ID + " LONG REFERENCES " + VehicleISchema.VEHICLE_TABLE;


    String[] INSURANCE_COLUMNS = new String[] {
            INSURANCE_ID,
            INSURANCE_INSURANCE_COMPANY_ID,
            INSURANCE_BROKER_ID,
            INSURANCE_INSURANCE_TYPE,
            INSURANCE_DESCRIPTION,
            INSURANCE_INSURANCE_POLICY,
            INSURANCE_ISSUANCE_DATE,
            INSURANCE_INITIAL_EFFECTIVE_DATE,
            INSURANCE_FINAL_EFFECTIVE_DATE,
            INSURANCE_NET_PREMIUM_VALUE,
            INSURANCE_TAX_AMOUNT,
            INSURANCE_TOTAL_PREMIUM_VALUE,
            INSURANCE_INSURANCE_DEDUCTIBLE,
            INSURANCE_BONUS_CLASS,
            INSURANCE_NOTE,
            INSURANCE_STATUS,
            INSURANCE_TRAVEL_ID,
            INSURANCE_VEHICLE_ID
    };
}

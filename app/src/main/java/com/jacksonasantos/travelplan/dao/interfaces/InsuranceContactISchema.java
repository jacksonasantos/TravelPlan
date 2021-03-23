package com.jacksonasantos.travelplan.dao.interfaces;

public interface InsuranceContactISchema {

    String INSURANCE_CONTACT_TABLE = "insurance_contact";

    String INSURANCE_CONTACT_ID = "id";
    String INSURANCE_CONTACT_INSURANCE_ID = "insurance_id";
    String INSURANCE_CONTACT_TYPE_CONTACT = "type_contact";
    String INSURANCE_CONTACT_DESCRIPTION_CONTACT = "description_contact";
    String INSURANCE_CONTACT_DETAIL_CONTACT = "detail_contact";

    // Version 32
    String CREATE_TABLE_INSURANCE_CONTACT_V32 = "CREATE TABLE IF NOT EXISTS "
            + INSURANCE_CONTACT_TABLE + " ("
            + INSURANCE_CONTACT_ID + " INTEGER PRIMARY KEY, "
            + INSURANCE_CONTACT_INSURANCE_ID + " INTEGER, "
            + INSURANCE_CONTACT_TYPE_CONTACT + " TEXT, "
            + INSURANCE_CONTACT_DESCRIPTION_CONTACT + " TEXT, "
            + INSURANCE_CONTACT_DETAIL_CONTACT + " TEXT "
            + ")";

    String[] INSURANCE_CONTACT_COLUMNS = new String[] {
            INSURANCE_CONTACT_ID,
            INSURANCE_CONTACT_INSURANCE_ID,
            INSURANCE_CONTACT_TYPE_CONTACT,
            INSURANCE_CONTACT_DESCRIPTION_CONTACT,
            INSURANCE_CONTACT_DETAIL_CONTACT
   };
}

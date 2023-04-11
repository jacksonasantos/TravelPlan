package com.jacksonasantos.travelplan.dao.interfaces;

public interface PersonISchema {

    String PERSON_TABLE = "person";

    String PERSON_ID = "id";
    String PERSON_NAME = "name";
    String PERSON_SHORT_NAME = "short_name";
    String PERSON_GENDER = "gender";
    String PERSON_DATE_BIRTH = "date_birth";
    String PERSON_DRIVING_RECORD = "driving_record";
    String PERSON_LICENSE_EXPIRATION_DATE = "license_expiration_date";
    String PERSON_FIRST_LICENSE_DATE = "first_license_date";
    String PERSON_LICENSE_ISSUE_DATE = "license_issue_date";
    String PERSON_LICENSE_CATEGORY = "license_category";

    // Version 59
    String CREATE_TABLE_PERSON_V59 = "CREATE TABLE IF NOT EXISTS "
            + PERSON_TABLE + " ("
            + PERSON_ID + " INTEGER PRIMARY KEY, "
            + PERSON_NAME + " TEXT, "
            + PERSON_SHORT_NAME + " TEXT, "
            + PERSON_GENDER + " INT, "
            + PERSON_DATE_BIRTH + " DATE, "
            + PERSON_DRIVING_RECORD + " TEXT, "
            + PERSON_LICENSE_EXPIRATION_DATE + " DATE, "
            + PERSON_FIRST_LICENSE_DATE + " DATE, "
            + PERSON_LICENSE_ISSUE_DATE + " DATE, "
            + PERSON_LICENSE_CATEGORY + " TEXT"
            + ")";

    String[] PERSON_COLUMNS = new String[] {
            PERSON_ID,
            PERSON_NAME,
            PERSON_SHORT_NAME,
            PERSON_GENDER,
            PERSON_DATE_BIRTH,
            PERSON_DRIVING_RECORD,
            PERSON_LICENSE_EXPIRATION_DATE,
            PERSON_FIRST_LICENSE_DATE,
            PERSON_LICENSE_ISSUE_DATE,
            PERSON_LICENSE_CATEGORY
    };
}

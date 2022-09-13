package com.jacksonasantos.travelplan.dao.interfaces;

public interface DriverISchema {

    String DRIVER_TABLE = "driver";

    String DRIVER_ID = "id";
    String DRIVER_NAME = "name";
    String DRIVER_SHORT_NAME = "short_name";
    String DRIVER_GENDER = "gender";
    String DRIVER_DRIVING_RECORD = "driving_record";
    String DRIVER_LICENSE_EXPIRATION_DATE = "licence_expiration_date";
    String DRIVER_FIRST_LICENSE_DATE = "first_licence_date";
    String DRIVER_LICENCE_ISSUE_DATE = "licence_issue_date";
    String DRIVER_CATEGORY = "category";

    // Version 47
    String CREATE_TABLE_DRIVER_V47 = "CREATE TABLE IF NOT EXISTS "
            + DRIVER_TABLE + " ("
            + DRIVER_ID + " INTEGER PRIMARY KEY, "
            + DRIVER_NAME + " TEXT, "
            + DRIVER_SHORT_NAME + " TEXT, "
            + DRIVER_GENDER + " INT, "
            + DRIVER_DRIVING_RECORD + " TEXT, "
            + DRIVER_LICENSE_EXPIRATION_DATE + " DATE, "
            + DRIVER_FIRST_LICENSE_DATE + " DATE, "
            + DRIVER_LICENCE_ISSUE_DATE + " DATE, "
            + DRIVER_CATEGORY + " TEXT"
            + ")";

    String[] DRIVER_COLUMNS = new String[] {
            DRIVER_ID,
            DRIVER_NAME,
            DRIVER_SHORT_NAME,
            DRIVER_GENDER,
            DRIVER_DRIVING_RECORD,
            DRIVER_LICENSE_EXPIRATION_DATE,
            DRIVER_FIRST_LICENSE_DATE,
            DRIVER_LICENCE_ISSUE_DATE,
            DRIVER_CATEGORY
    };
}

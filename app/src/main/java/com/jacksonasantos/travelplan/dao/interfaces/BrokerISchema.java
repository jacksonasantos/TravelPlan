package com.jacksonasantos.travelplan.dao.interfaces;

public interface BrokerISchema {

    String BROKER_TABLE = "broker";

    String BROKER_ID = "id";
    String BROKER_NAME = "name";
    String BROKER_PHONE = "phone";
    String BROKER_EMAIL = "email";
    String BROKER_CONTACT_NAME = "contact_name";

    // Version 16
    String CREATE_TABLE_BROKER_V16 = "CREATE TABLE IF NOT EXISTS "
            + BROKER_TABLE + " ("
            + BROKER_ID + " INTEGER PRIMARY KEY, "
            + BROKER_NAME + " TEXT, "
            + BROKER_PHONE + " TEXT, "
            + BROKER_EMAIL + " TEXT "
            + ")";

    // Version 24
    String ALTER_TABLE_BROKER_V24 = "ALTER TABLE " + BROKER_TABLE
            + " ADD COLUMN " + BROKER_CONTACT_NAME + " TEXT ";

    String[] BROKER_COLUMNS = new String[] {
            BROKER_ID,
            BROKER_NAME,
            BROKER_PHONE,
            BROKER_EMAIL,
            BROKER_CONTACT_NAME
    };
}

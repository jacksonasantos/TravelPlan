package com.jacksonasantos.travelplan.dao.interfaces;

public interface BrokerISchema {

    String BROKER_TABLE = "broker";

    String BROKER_ID = "rowid";
    String BROKER_NAME = "name";
    String BROKER_PHONE = "phone";
    String BROKER_EMAIL = "email";

    // Version 16
    String CREATE_TABLE_BROKER_V16 = "CREATE TABLE IF NOT EXISTS "
            + BROKER_TABLE + " ("
            + BROKER_NAME + " TEXT, "
            + BROKER_PHONE + " TEXT, "
            + BROKER_EMAIL + " TEXT "
            + ")";

    String[] BROKER_COLUMNS = new String[] {
            BROKER_ID,
            BROKER_NAME,
            BROKER_PHONE,
            BROKER_EMAIL
    };
}

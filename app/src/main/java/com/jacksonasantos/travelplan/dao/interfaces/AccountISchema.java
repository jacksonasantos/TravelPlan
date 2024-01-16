package com.jacksonasantos.travelplan.dao.interfaces;

public interface AccountISchema {

    String ACCOUNT_TABLE = "account";

    String ACCOUNT_ID = "id";
    String ACCOUNT_ACCOUNT_TYPE = "account_type";
    String ACCOUNT_DESCRIPTION = "description";

    // Version 81_1
    String CREATE_TABLE_ACCOUNT_V81_1 = "CREATE TABLE IF NOT EXISTS "
            + ACCOUNT_TABLE + " ("
            + ACCOUNT_ID + " INTEGER PRIMARY KEY, "
            + ACCOUNT_ACCOUNT_TYPE + " INT, "
            + ACCOUNT_DESCRIPTION + " TEXT "
            + ")";

    String[] ACCOUNT_COLUMNS = new String[] {
            ACCOUNT_ID,
            ACCOUNT_ACCOUNT_TYPE,
            ACCOUNT_DESCRIPTION
    };
}

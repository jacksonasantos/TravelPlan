package com.jacksonasantos.travelplan.DAO.Interface;

public interface UserISchema {

    String USER_TABLE = "users";

    String USER_ID = "rowid";
    String USER_NAME = "user_name";
    String USER_EMAIL = "email";
    String USER_DATE = "created_date";

    String CREATE_TABLE_USER_V4 = "CREATE TABLE IF NOT EXISTS "
                                + USER_TABLE + " ("
                                + USER_ID + " INTEGER PRIMARY KEY, "
                                + USER_NAME + " TEXT NOT NULL, "
                                + USER_EMAIL + " TEXT, "
                                + USER_DATE + " BIGINT "
                                + ")";

    String[] USER_COLUMNS = new String[] { USER_ID, USER_NAME, USER_EMAIL, USER_DATE };
}